![eaglai-high-resolution-logo](https://github.com/PedroDSFerreira/eagl.ai/assets/97121697/b4f54272-73a3-4926-a9ab-0f1c0c01da60)

>Search and identify contacts by physical traits, gathered from pictures with AI.

## Requirements
- Maven
- Java 21
- Docker/Docker Compose
- `make` command

## Getting Started

1. Create a local `.env` file

```
make prepare-env
```

2. Build maven project and container images

```
make build
```

3. Run containers

```
make up
```

4. Install Ollama model

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
