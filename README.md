![eaglai-high-resolution-logo-grayscale-transparent](https://github.com/user-attachments/assets/08681004-7f49-44a8-a04d-2c8b20ce88ce)

<br>

>Search and identify contacts by physical traits, gathered from pictures with AI.

## Requirements
- Maven
- Java 21
- Docker/Docker Compose
- `make` command

## Getting Started

#### 1. Create a local `.env` file

```
make prepare-env
```

#### 2. Build maven project and container images

##### With GPU support (for NVIDIA only):

- Install the NVIDIA [Container Toolkit](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/latest/install-guide.html#installation).

```
make build-gpu
```

##### Without GPU support:

```
make build
```

#### 3. Run containers

```
make up
```

#### 4. Install Ollama model

```
docker exec -it ollama ollama run <ollama-model>
```

Currently, the available models are:
- `llava:7b`
- `llava:13b`
- `llava:34b` (default)


If changed, update `.env` with model used.

## License
This project is licensed under the [MIT License](LICENSE).
