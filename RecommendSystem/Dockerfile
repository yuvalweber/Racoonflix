FROM gcc:latest AS builder

RUN apt-get update && apt-get install -y cmake && apt clean

WORKDIR /usr/src/app
COPY . .

WORKDIR /usr/src/app/src/build

RUN --mount=type=cache,target=/usr/src/app/src/build cmake .. && \
                                                           make && \
                                                           mv ./Netflix /usr/src/app/Netflix
ENTRYPOINT ["/usr/src/app/Netflix"]