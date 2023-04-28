FROM openbis/debian-openbis:latest

COPY check_health.sh .
RUN chmod +x check_health.sh
HEALTHCHECK --interval=10s --timeout=3s CMD ./check_health.sh https://localhost:443