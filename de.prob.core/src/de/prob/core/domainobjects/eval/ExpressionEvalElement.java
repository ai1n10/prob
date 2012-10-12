/**
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, Heinrich
 * Heine Universitaet Duesseldorf This software is licenced under EPL 1.0
 * (http://www.eclipse.org/org/documents/epl-v10.html)
 * */

package de.prob.core.domainobjects.eval;

import java.util.LinkedList;

import org.eventb.core.ast.Expression;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BException;
import de.be4.classicalb.core.parser.node.AExpressionParseUnit;
import de.be4.classicalb.core.parser.node.EOF;
import de.be4.classicalb.core.parser.node.Start;
import de.prob.eventb.translator.ExpressionVisitor;

public final class ExpressionEvalElement extends AbstractEvalElement {

	private final Start parse;
	private final String expression;

	public static ExpressionEvalElement fromRodin(final Expression expression)
			throws BException {
		if (expression == null) {
			String message = "Expression input must not be null";
			throw new BException("", new NullPointerException(message));
		}
		ExpressionVisitor ev = new ExpressionVisitor(new LinkedList<String>());
		expression.accept(ev);
		AExpressionParseUnit epu = new AExpressionParseUnit(ev.getExpression());
		Start start = new Start(epu, new EOF());
		return new ExpressionEvalElement(expression, start);
	}

	public static ExpressionEvalElement create(final String expression)
			throws BException {
		return new ExpressionEvalElement(expression);
	}

	public ExpressionEvalElement(final String expression) throws BException {
		this.expression = expression;
		parse = parse(BParser.EXPRESSION_PREFIX, expression);
	}

	public ExpressionEvalElement(final Expression expression, final Start start) {
		this.expression = expression.toString();
		this.parse = start;
	}

	@Override
	public Start getPrologAst() {
		return parse;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public String getLabel() {
		return expression;
	}

}
