/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.coreedge.core.consensus;

import io.netty.channel.ChannelHandlerContext;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.neo4j.coreedge.VersionCheckerChannelInboundHandler;
import org.neo4j.coreedge.messaging.Inbound;
import org.neo4j.coreedge.messaging.Message;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;

import static java.lang.String.format;

class RaftMessageHandler extends VersionCheckerChannelInboundHandler<RaftMessages.StoreIdAwareMessage>
{
    private final Supplier<Inbound.MessageHandler<RaftMessages.StoreIdAwareMessage>> messageHandler;
    private final Log log;

    RaftMessageHandler( Predicate<Message> versionChecker,
            Supplier<Inbound.MessageHandler<RaftMessages.StoreIdAwareMessage>> messageHandler, LogProvider logProvider )
    {
        super( versionChecker, logProvider );
        this.messageHandler = messageHandler;
        this.log = logProvider.getLog( getClass() );
    }

    @Override
    protected void doChannelRead0( ChannelHandlerContext channelHandlerContext,
            RaftMessages.StoreIdAwareMessage storeIdAwareMessage ) throws Exception
    {
        try
        {
            messageHandler.get().handle( storeIdAwareMessage );
        }
        catch ( Exception e )
        {
            log.error( format( "Failed to process message %s", storeIdAwareMessage ), e );
        }
    }
}