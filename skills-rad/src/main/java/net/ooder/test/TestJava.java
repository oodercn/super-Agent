package net.ooder.test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

public class TestJava {
    public static void main(String[] args) throws FileNotFoundException {
        // parse() 参数可以是 String, File, InputStream等
        CompilationUnit cu = StaticJavaParser.parse(new File("E:\\ooder\\esd\\target\\export\\test\\java\\com\\ds\\iorg\\leavetable\\module\\Leavetable.java"));

        List<ConstructorDeclaration> mds = cu.findAll(ConstructorDeclaration.class);
        cu.getPackageDeclaration().get().setName("test");

        for (Iterator<ImportDeclaration> it = cu.getImports().iterator(); it.hasNext(); ) {
            ImportDeclaration md = it.next();
            md.setName(md.getName());
            System.out.println(md.toString());
        }
        // cu.findAll()

        cu.getTypes();

        // ClassOrInterfaceDeclaration cu.getClassByName("Leavetable").get().setName("Test2");

        System.out.println(cu.toString());
        mds.forEach(md -> System.out.println(md.toString() + "\n------------------------------\n"));
    }


}
