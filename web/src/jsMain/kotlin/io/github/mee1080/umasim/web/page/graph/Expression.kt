/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org/>
 */
package io.github.mee1080.umasim.web.page.graph

import kotlin.math.*

/*
expression = factor , { operator , factor }

operator = "+" | "-" | "*" | "/" | "%" | "^"

factor = number | variable | function | "(" , expression , ")"

number = [ "-" ] , digit , { digit } , [ "." , digit , {digit} ]
digit = "0" | ... | "9"

variable = letter , { letter | digit }
letter = "A" | ... | "z"

function = variable , "(" , expression , [ "," , expression ] , ")"
 */
class Expression private constructor(private val tokens: List<TokenOrBracket>) {

    constructor(expression: String) : this(parse(expression))

    private class Operator(
        val token: String,
        val level: Int,
        val calc: (Double, Double) -> Double,
    )

    companion object {

        private val operators: Map<String, Operator> = listOf(
            Operator("+", 1) { a, b -> a + b },
            Operator("-", 1) { a, b -> a - b },
            Operator("*", 2) { a, b -> a * b },
            Operator("/", 2) { a, b -> a / b },
            Operator("%", 2) { a, b -> a % b },
            Operator("^", 2) { a, b -> a.pow(b) },
        ).associateBy { it.token }

        private val defaultFunctions: Map<String, (List<Double>) -> Double> = mapOf(
            "max" to { it.max() },
            "min" to { it.min() },
            "sqrt" to { sqrt(it.first()) },
            "log" to { log(it.first(), it.getOrElse(1) { 10.0 }) },
            "log10" to { log10(it.first()) },
            "log2" to { log2(it.first()) },
            "floor" to { floor(it.first()) },
            "ceil" to { ceil(it.first()) },
            "round" to { round(it.first()) },
            "abs" to { abs(it.first()) },
        )

        fun parseOrNull(
            expression: String,
            onError: (Throwable) -> Unit = {},
        ) = kotlin.runCatching { Expression(expression) }.onFailure(onError).getOrNull()

        private fun parse(expression: String): List<TokenOrBracket> {
            val stack = mutableListOf<Pair<MutableList<TokenOrBracketOrComma>, String>>().apply {
                add(mutableListOf<TokenOrBracketOrComma>() to "")
            }
            val numberBuilder = StringBuilder()
            val variableBuilder = StringBuilder()

            expression.forEach { c ->
                if (c.isWhitespace()) {
                    // ignore
                } else if (c == '.' || (variableBuilder.isEmpty() && c.isDigit()) || (numberBuilder.isEmpty() && variableBuilder.isEmpty() && c == '-')) {
                    numberBuilder.append(c)
                } else if (c == '(') {
                    stack.add(mutableListOf<TokenOrBracketOrComma>() to variableBuilder.toString())
                    variableBuilder.clear()
                } else if (c.isLetterOrDigit()) {
                    variableBuilder.append(c)
                } else {
                    if (numberBuilder.isNotEmpty()) {
                        stack.last().first.add(NumberToken(numberBuilder.toString().toDouble()))
                        numberBuilder.clear()
                    }
                    if (variableBuilder.isNotEmpty()) {
                        stack.last().first.add(VariableToken(variableBuilder.toString()))
                        variableBuilder.clear()
                    }
                    if (c == ')') {
                        if (stack.size < 2) throw IllegalArgumentException("brackets not match")
                        val last = stack.removeLast()
                        val token = if (last.second.isEmpty()) {
                            Bracket(last.first.filterIsInstance<TokenOrBracket>())
                        } else {
                            val arguments = mutableListOf<MutableList<TokenOrBracket>>().apply {
                                add(mutableListOf())
                            }
                            last.first.forEach {
                                if (it is TokenOrBracket) {
                                    arguments.last().add(it)
                                } else {
                                    arguments.add(mutableListOf())
                                }
                            }
                            FunctionToken(last.second, arguments)
                        }
                        stack.last().first.add(token)
                    } else if (c == ',') {
                        stack.last().first.add(CommaToken)
                    } else {
                        val operator = operators[c.toString()]
                        if (operator != null) {
                            stack.last().first.add(OperatorToken(operator))
                        }
                    }
                }
            }
            if (stack.size > 1) throw IllegalArgumentException("Brackets not match")
            if (numberBuilder.isNotEmpty()) {
                stack.last().first.add(NumberToken(numberBuilder.toString().toDouble()))
            }
            if (variableBuilder.isNotEmpty()) {
                stack.last().first.add(VariableToken(variableBuilder.toString()))
            }
            return stack.last().first.filterIsInstance<TokenOrBracket>()
        }
    }

    private val reversePolishNotation: List<Token> = buildList {
        val operatorStack = mutableListOf<OperatorToken>()
        tokens.forEach { token ->
            when (token) {
                is Bracket -> {
                    addAll(Expression(token.expression).reversePolishNotation)
                }

                is OperatorToken -> {
                    val last = operatorStack.lastOrNull()
                    if (last != null && last >= token) {
                        addAll(operatorStack.reversed())
                        operatorStack.clear()
                    }
                    operatorStack.add(token)
                }

                is NumberToken -> {
                    add(token)
                }

                is VariableToken -> {
                    add(token)
                }

                is FunctionToken -> {
                    add(token)
                }
            }
        }
        addAll(operatorStack.reversed())
    }

    fun calcOrNull(
        variables: Map<String, Double> = emptyMap(),
        userFunctions: Map<String, (List<Double>) -> Double> = emptyMap(),
        allowNan: Boolean = false,
        allowInfinite: Boolean = false,
        onError: (Throwable) -> Unit = {},
    ) = kotlin.runCatching {
        calc(variables, userFunctions, allowNan, allowInfinite)
    }.onFailure(onError).getOrNull()

    fun calc(
        variables: Map<String, Double> = emptyMap(),
        userFunctions: Map<String, (List<Double>) -> Double> = emptyMap(),
        allowNan: Boolean = false,
        allowInfinite: Boolean = false,
    ): Double {
        val functions = defaultFunctions + userFunctions
        val stack = mutableListOf<Double>()
        reversePolishNotation.forEach { token ->
            when (token) {
                is NumberToken -> {
                    stack.add(token.number)
                }

                is VariableToken -> {
                    val number = variables[token.name]
                        ?: throw IllegalArgumentException("variable $token not exists")
                    stack.add(number)
                }

                is OperatorToken -> {
                    if (stack.size < 2) throw IllegalArgumentException("no operand for $token in $this")
                    val number2 = stack.removeLast()
                    val number1 = stack.removeLast()
                    stack.add(token.operator.calc(number1, number2))
                }

                is FunctionToken -> {
                    val function = functions[token.name]
                        ?: throw IllegalArgumentException("function ${token.name} not exists")
                    val number = function.invoke(token.expressions.map {
                        it.calc(variables, functions)
                    })
                    stack.add(number)
                }
            }
        }
        if (stack.size != 1) throw IllegalArgumentException("operator and operand count error in $this")
        val result = stack.first()
        if (!allowNan && result.isNaN()) throw ArithmeticException("result is Not a Number")
        if (!allowInfinite && result.isInfinite()) throw ArithmeticException("result is Infinity")
        return result
    }

    override fun toString() = tokens.joinToString(" ")

    private sealed interface TokenOrBracketOrComma

    private sealed interface TokenOrBracket : TokenOrBracketOrComma

    private sealed interface Token : TokenOrBracket

    private class NumberToken(val number: Double) : Token {
        override fun toString() = number.toString()
    }

    private class OperatorToken(val operator: Operator) : Token, Comparable<OperatorToken> {
        override fun toString() = operator.token

        override fun compareTo(other: OperatorToken) = operator.level - other.operator.level
    }

    private class VariableToken(val name: String) : Token {
        override fun toString() = name
    }

    private data object CommaToken : TokenOrBracketOrComma

    private class Bracket(val expression: List<TokenOrBracket>) : TokenOrBracket {
        override fun toString() = "(${expression.joinToString(" ")})"
    }

    private class FunctionToken(val name: String, tokens: List<List<TokenOrBracket>>) : Token {
        val expressions = tokens.map { Expression(it) }
        override fun toString() = "$name(${expressions.joinToString(", ")})"
    }
}
