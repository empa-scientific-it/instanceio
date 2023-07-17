FROM openbis/debian-openbis:20.10.7

COPY check_health.sh .
RUN chmod +x check_health.sh
HEALTHCHECK --interval=1s --timeout=0.5s CMD ./check_health.sh https://localhost:443