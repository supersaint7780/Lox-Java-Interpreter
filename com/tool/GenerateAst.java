package com.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    private static final int IncorrectCommandUsage = 64;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output_directory>");
            System.exit(IncorrectCommandUsage);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Assign: Token name, Expr value",
            "Binary: Expr left, Token operator, Expr right",
            "Ternary: Expr condition, Expr trueBranch, Expr falseBranch",
            "Grouping: Expr expression",
            "Literal: Object value",
            "Unary: Token operator, Expr right",
            "Variable: Token name"
        ));
        
        defineAst(outputDir, "Stmt", Arrays.asList(
            "Expression: Expr expression",
            "Print: Expr expression",
            "Var: Token name, Expr initializer"
        ));
    }

    private static void defineAst(
            String outputDir, String baseName, List<String> types) throws IOException {
                
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        writer.println();

        defineVisitor(writer, baseName, types);

        writer.println();

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
            writer.println();
        }

        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(
            PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");

        // constructor
        writer.println("        " + className + "(" + fieldList + ") {");

        // store parameters in fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }

        writer.println("        }");

        // Visitor pattern.
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");

        // fields
        writer.println();
        for (String field : fields) {
            writer.println("        final " + field + ";");
        }
        writer.println("    }");
    }

    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {

        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }
}