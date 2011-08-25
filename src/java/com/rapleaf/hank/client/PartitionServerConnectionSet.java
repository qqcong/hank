/*
 *  Copyright 2011 Rapleaf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.rapleaf.hank.client;

import com.rapleaf.hank.generated.HankExceptions;
import com.rapleaf.hank.generated.HankResponse;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PartitionServerConnectionSet {
  private static final HankResponse ZERO_REPLICAS = HankResponse.xception(HankExceptions.zero_replicas(true));

  private static final Logger LOG = Logger.getLogger(PartitionServerConnectionSet.class);

  private final List<PartitionServerConnection> connections = new ArrayList<PartitionServerConnection>();
  private final AtomicInteger nextIdx = new AtomicInteger(0);

  public PartitionServerConnectionSet(List<PartitionServerConnection> connections) {
    this.connections.addAll(connections);
  }

  public HankResponse get(int domainId, ByteBuffer key) throws TException {
    int numAttempts = 0;
    LOG.trace("There are " + connections.size() + " connections for domain id " + domainId);
    while (numAttempts < connections.size()) {
      numAttempts++;
      int pos = nextIdx.getAndIncrement() % connections.size();
      PartitionServerConnection connection = connections.get(pos);
      if (!connection.isAvailable()) {
        LOG.trace("Connection " + connection + " was not available, so skipped it.");
        continue;
      }
      try {
        return connection.get(domainId, key);
      } catch (IOException e) {
        LOG.trace("Failed to execute get() with connection " + connection + ", so skipped it.", e);
      }
    }
    if (numAttempts == connections.size()) {
      LOG.trace("None of the " + connections.size() + " connections were available.");
    }
    return ZERO_REPLICAS;
  }
}