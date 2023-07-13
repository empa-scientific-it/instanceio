FROM openbis/debian-openbis:20.10.6

COPY check_health.sh .
RUN chmod +x check_health.sh
HEALTHCHECK --interval=10s --timeout=3s CMD ./check_health.sh https://localhost:443