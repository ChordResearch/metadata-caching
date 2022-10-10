FROM node:16 as base

# Environment
ENV PORT=5001

WORKDIR /home/app

COPY package.json /home/app/
COPY lerna.json /home/app/

COPY packages/api/package.json /home/app/packages/api/
COPY packages/commons/package.json /home/app/packages/commons/

RUN yarn
RUN yarn install

# Build
COPY . /home/app/
RUN yarn build

EXPOSE ${PORT}

WORKDIR /home/app/packages/api

CMD ["node", "dist/index.js"]