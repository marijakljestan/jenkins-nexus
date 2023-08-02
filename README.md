# CI/CD pipeline

This project represents Jenkins CICD pipeline that: 
- Listen for a webhook coming in from GitLab branch (push event) 
- Build the app 
- Simulate tests 
- Create new docker image of the updated app and save that image in Nexus (artifact hosting) 
- Deploys that app 

## Running infrastructure
Run command <code>docker compose up --build</code> in root directory.

### Note
Deploying all of this in docker is done because gitlab website webhooks can't send requests to servers with a volatile IP address (and also won't talk to non-https servers). \ 
By having all images on the same network and having them have the same IP every time solves this issue.
