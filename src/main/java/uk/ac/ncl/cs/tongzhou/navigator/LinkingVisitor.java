package uk.ac.ncl.cs.tongzhou.navigator;

import com.github.javaparser.ast.DataKey;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;

import java.util.ArrayList;
import java.util.List;

public class LinkingVisitor extends VoidVisitorAdapter<JavaSymbolSolver> {

    public static final DataKey<String> LINK_STYLE = new DataKey<>() {};
    public static final DataKey<String> LINK_ID = new DataKey<>() {}; // for targets
    public static final DataKey<String> LINK_TO = new DataKey<>() {}; // for origins

    private final List<TypeDeclaration> typeDeclarations = new ArrayList<>();

    public List<TypeDeclaration> getTypeDeclarations() {
        return typeDeclarations;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, JavaSymbolSolver javaSymbolSolver) {

        typeDeclarations.add(node);

        ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = javaSymbolSolver.resolveDeclaration(node, ResolvedReferenceTypeDeclaration.class);
        String name = resolvedReferenceTypeDeclaration.getQualifiedName();

        node.setData(LINK_ID, name);
        node.setData(LINK_STYLE, "ClassOrInterfaceDeclaration");

        super.visit(node, javaSymbolSolver);
    }

    // TODO Decls for Enums and Annotations...

    @Override
    public void visit(ImportDeclaration node, JavaSymbolSolver javaSymbolSolver) {
        // TODO
    }

    @Override
    public void visit(ClassOrInterfaceType node, JavaSymbolSolver javaSymbolSolver) {

        SimpleName simpleName = node.getName();
        String fullName = node.getTokenRange().get().toString();
        simpleName.setData(LINK_TO, fullName);
        simpleName.setData(LINK_STYLE, "ClassOrInterfaceType");
    }
}

