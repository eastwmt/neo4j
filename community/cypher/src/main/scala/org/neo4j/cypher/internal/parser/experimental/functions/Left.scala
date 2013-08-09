/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.parser.experimental.functions

import org.neo4j.cypher.internal.parser.experimental._
import org.neo4j.cypher.internal.symbols._
import org.neo4j.cypher.internal.commands.{expressions => commandexpressions}

case object Left extends Function {
  def name = "LEFT"

  def semanticCheck(ctx: ast.Expression.SemanticContext, invocation: ast.FunctionInvocation) : SemanticCheck =
    checkArgs(invocation, 2) ifOkThen {
      invocation.arguments(0).constrainType(StringType()) then
      invocation.arguments(1).constrainType(LongType())
    } then invocation.specifyType(StringType())

  def toCommand(invocation: ast.FunctionInvocation) = {
    val commands = invocation.arguments.map(_.toCommand)
    commandexpressions.LeftFunction(commands(0), commands(1))
  }
}
