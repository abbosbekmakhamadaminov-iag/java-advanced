#!/usr/bin/env python3

import grpc
import threading
import time
from concurrent import futures

# Import generated classes (you'll need to generate these from the proto file)
import ping_pong_pb2
import ping_pong_pb2_grpc


class PythonGrpcClient:
    def __init__(self, host='localhost', port=8080):
        self.host = host
        self.port = port
        self.channel = grpc.insecure_channel(f'{host}:{port}')
        self.stub = ping_pong_pb2_grpc.PingPongServiceStub(self.channel)

    def send_ping(self, message):
        """Send a simple ping request and receive pong response"""
        print(f"Sending ping: {message}")

        request = ping_pong_pb2.PingRequest(
            message=message,
            timestamp=int(time.time() * 1000),  # milliseconds
            client_id="python-client-001"
        )

        try:
            response = self.stub.SendPing(request)
            print(f"Received pong: {response.message}")
            print(f"Server ID: {response.server_id}")
            print(f"Success: {response.success}")
            print(f"Server timestamp: {response.timestamp}")
            return response
        except grpc.RpcError as e:
            print(f"RPC failed: {e.code()} - {e.details()}")
            return None

    def close(self):
        """Close the gRPC channel"""
        self.channel.close()


def main():
    # Create client
    client = PythonGrpcClient()

    try:
        # Test simple ping-pong
        print("=== Testing Simple Ping-Pong ===")
        client.send_ping("Ping")
        client.send_ping("Hello from Python!")

    except KeyboardInterrupt:
        print("\nClient interrupted")
    finally:
        client.close()


if __name__ == '__main__':
    main()
