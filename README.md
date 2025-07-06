![eaglai-high-resolution-logo-grayscale-transparent](https://github.com/user-attachments/assets/08681004-7f49-44a8-a04d-2c8b20ce88ce)

<br>

>Search and identify contacts by physical traits, gathered from pictures with AI.

## Getting Started

### Requirements
- Docker/Docker Compose
- `make` command

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

## Ollama models

This project uses a vision-capable (multimodal) model by default. You can explore all available options [here](https://ollama.com/search?c=vision) to find one that best fits your use case.

By default, the project uses `gemma3:12b-it-qat`. To change it, just update `OLLAMA_MODEL` in your `.env` or shell. 

Make sure to pick a model size that your GPU/CPU and memory can comfortably support.

## Demo

You can try the project online at: [https://eagle-ai.griffin-frog.ts.net/](https://eagle-ai.griffin-frog.ts.net/).

- **Database refresh:** The database is automatically refreshed every hour. All data will be reset at that time.
- **Mock Ollama responses:** This demo uses the [mock configuration](docker-compose-mock.yaml), so the Ollama API responses are static and not representative of any actual parsed facial features.

> ⚠️ **Warning:**
> Do **not** enter any personal or sensitive information. All data is temporary and publicly accessible.

## License
This project is licensed under the [MIT License](LICENSE).
