
# Generate Python gRPC files
python3.11 -m grpc_tools.protoc \
    --proto_path=../src/main/proto \
    --python_out=. \
    --grpc_python_out=. \
    ../src/main/proto/ping_pong.proto

# Verify the files were created
ls -la *.py