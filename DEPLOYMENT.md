# OpenELIS LabBridge Deployment Guide

## Overview
This repository contains a customized OpenELIS with LabBridge API endpoints for external system integration.

## Custom Features
- **Pending Tests API**: `GET /openelis/pendingtests.do`
- **Update Test Results API**: `POST /openelis/updatetestresult.do`
- **Health Check API**: `GET /openelis/test.do`

## Quick Start

### Prerequisites
- Docker Desktop (Windows/Mac) or Docker + Docker Compose (Linux)
- Git

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd OpenElis
   ```

2. **Start the application**
   ```bash
   docker compose up -d
   ```

3. **Access the application**
   - OpenELIS UI: http://localhost:8052/openelis
   - API Base URL: http://localhost:8052/openelis

### API Usage Examples

#### Get Pending Tests
```bash
curl http://localhost:8052/openelis/pendingtests.do
```

#### Update Test Result
```bash
curl -X POST http://localhost:8052/openelis/updatetestresult.do \
  -H 'Content-Type: application/json' \
  -d '{"analysisId":"123", "testId":"456", "resultValue":"7.5", "resultType":"N"}'
```

## Development

### Building Custom Image
If you need to rebuild the custom image with changes:

```bash
# Build the application
ant -Dskip.jasper=true -Dskip.compass=true dist

# Build Docker image
docker build -t labbridge/openelis:local -f package/docker/openelis/Dockerfile .

# Restart services
docker compose down && docker compose up -d
```

### Publishing to Registry

#### Docker Hub
```bash
docker tag labbridge/openelis:local yourusername/openelis-labbridge:latest
docker push yourusername/openelis-labbridge:latest
```

#### GitHub Container Registry
```bash
docker tag labbridge/openelis:local ghcr.io/yourusername/openelis-labbridge:latest
docker push ghcr.io/yourusername/openelis-labbridge:latest
```

Then update `docker-compose.yml`:
```yaml
openelis:
  image: yourusername/openelis-labbridge:latest  # or ghcr.io/...
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8052 and 5432 are not in use
2. **Docker permissions**: On Linux, add user to docker group
3. **Build failures**: Ensure Java 8+ and Ant are available for development

### Logs
```bash
# View application logs
docker compose logs openelis

# View database logs  
docker compose logs openelisdb
```

## Architecture

The custom implementation uses:
- **Direct SQL approach** for result updates (bypasses Hibernate audit trail issues)
- **OpenELIS existing connection pool** for database access
- **Status management** that properly transitions tests from pending to finalized

## Support

For issues related to:
- **OpenELIS core functionality**: Refer to original OpenELIS documentation
- **LabBridge API endpoints**: Check this repository's issues section
