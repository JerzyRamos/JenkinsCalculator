package edu.byu.cs329.constantpropagation;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;


public class ConstantPropagator {

    ControlFlowGraph cfg;
    ReachingDefinitions rd;

    public ConstantPropagator(ControlFlowGraph cfg, ReachingDefinitions rd) {
        this.cfg = cfg;
        this.rd = rd;
    }

    public boolean propagate() {
        /* Replace any use of a variable that has a single reaching definition
           that is a literal with the literal (or if there are multiple definitions
           that reach the use but both are the same literal)
        */

        Statement root = controlFlowGraph.getStart();
        boolean changed = false;

        /*
        a=3;
        a=3;
        
        b=a => b=3
        */
        // create a linked list and push root
        Set<Statement> usedNodes = new HashSet<>();
        LinkedList<Statement> nodes = new LinkedList<>();
        nodes.add(root);

        while (nodes.size() > 0) {

            // remove first node from linked list
            Statement node = linkedList.removeFirst();
            usedNodes.add(node);

            // push first node's children to linked list
            succs = cfg.getSuccs(node);
            for (Statement s : succs) {
                if (!usedNodes.contains(s)) {
                    nodes.add(s);
                }
            }

            // get right hand side definitions
            Expression rightHandSide = getRhs(node);

            if (rightHandSide == null) {
                continue;
            }

            Set<Definition> definitions = reachingDefinitions.getReachingDefinitions(rightHandSide);

            // check if definitions all contain same literal
            Statement literal = null; // first literal
            for (Definition d : definitions) {
                Statement statement = d.statement;
                Expression rhs = getRhs(statement);
                if (rightHandSide == null) {
                    continue;
                }

                // check if literal
                boolean isLiteral = isLiteralExpression(rhs);
                if (!isLiteral) {
                    continue;
                }

                // if literal...
                if (literal == null) {
                    // set literal
                    literal = rhs;
                } else {
                    // check if literal is same
                    if (literal != rhs) {
                        continue;
                    }
                }
            }

            if (literal == null) {
                continue;
            }

            // if all definitions contain same literal, replace rhs with literal
            changed = true;
            setRhs(node, literal);
        }

        return changed;
    }

    private boolean isLiteralExpression(ASTNode exp) {
        return (exp instanceof BooleanLiteral)
                || (exp instanceof CharacterLiteral)
                || (exp instanceof NullLiteral)
                || (exp instanceof StringLiteral)
                || (exp instanceof TypeLiteral)
                || (exp instanceof NumberLiteral);
    }

    private Expression getRhs(Statement statement) {
        if (!(statement instanceof VariableDeclarationStatement
                || statement instanceof ExpressionStatement)) {
            return null;
        }
        Expression rightHandSide;
        if (statement instanceof VariableDeclarationStatement) {
            rightHandSide = ((VariableDeclarationFragment)
                    ((VariableDeclarationStatement) statement)
                            .fragments().get(0))
                    .getInitializer();
        } else {
            rightHandSide =  ((Assignment)
                    ((ExpressionStatement) statement).getExpression()).getRightHandSide();
        }
        return rightHandSide;
    }

    private void setRhs(Statement statement, Expression value) {
        Expression rightHandSide;
        if (statement instanceof VariableDeclarationStatement) {
            rightHandSide = ((VariableDeclarationFragment)
                    ((VariableDeclarationStatement) statement)
                            .fragments().get(0))
                    .setInitializer(value);
        } else {
            ((Assignment)
                    ((ExpressionStatement) statement).getExpression()).setRightHandSide(value);
        }
    }
}