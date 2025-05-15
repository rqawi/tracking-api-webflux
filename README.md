## Run Locally with Docker Compose
```bash
docker-compose up -d
```
This will start Redis service locally.

## GCP Cloud Build
To trigger a build and deploy pipeline on GCP:
```bash
gcloud builds submit --config cloudbuild.yaml
```
Ensure you’ve set up:
- Artifact Registry or Container Registry
- GKE cluster with appropriate IAM roles
- `kubectl` context pointing to your GKE cluster