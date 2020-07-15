/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.nacos.core.remote.grpc;

import com.alibaba.nacos.api.grpc.GrpcMetadata;
import com.alibaba.nacos.api.grpc.GrpcRequest;
import com.alibaba.nacos.api.grpc.GrpcResponse;
import com.alibaba.nacos.api.grpc.RequestStreamGrpc;
import com.alibaba.nacos.api.remote.connection.Connection;
import com.alibaba.nacos.api.remote.connection.ConnectionMetaInfo;
import com.alibaba.nacos.core.remote.ConnectionManager;
import com.alibaba.nacos.core.utils.Loggers;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuzunfei
 * @version $Id: GrpcStreamRequestHanderImpl.java, v 0.1 2020年07月13日 7:30 PM liuzunfei Exp $
 */
@Service
public class GrpcStreamRequestHanderImpl extends RequestStreamGrpc.RequestStreamImplBase{

    @Autowired
    ConnectionManager connectionManager;



    @Override
    public void requestStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
        Loggers.GRPC.info(" gRpc Server receive stream :"+request);
        GrpcMetadata metadata = request.getMetadata();
        String clientIp = metadata.getClientIp();
        String connectionId = metadata.getConnectionId();

        ConnectionMetaInfo metaInfo=new ConnectionMetaInfo(connectionId,clientIp,"GRPC");
        Connection connection=new GrpcConnection(metaInfo,responseObserver);
        connectionManager.register(connectionId,connection);
    }
}