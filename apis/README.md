## Install

Clone the repository:

```sh
git clone https://github.com/narendracode/project-starter.git <project-name>
cd <project-name>
```

Install the dependencies:

```sh
yarn
```

## Available Scripts

In the root directory, you can run:

### `yarn build`

Build all packages:

```sh
yarn build
```

Build a single package:

```sh
yarn build --scope=<package-name>
```

### `yarn clean`

Delete build artifacts for all packages:

```sh
yarn clean
```

Clean a single package:

```sh
yarn clean --scope=<package-name>
```


### `yarn create-package`

Create a package:

```sh
yarn create-package
```

Create a package using the CLI:

```sh
yarn create-package <package-name> --template=<template>
```

Create package `api` using the TypeScript template (default):

```sh
yarn create-package api --template=typescript
```

Create package `portal` using the React template:

```sh
yarn create-package portal --template=react
```

### `yarn lint`

Lint all packages:

```sh
yarn lint
```

Lint a single package:

```sh
yarn lint --scope=<package-name>
```

For example:

```sh
yarn lint --scope=example-a
```

### `yarn lint:fix`

Fix lint errors for all packages:

```sh
yarn lint:fix
```

Fix lint errors for a single package:

```sh
yarn lint:fix --scope=<package-name>
```

For example:

```sh
yarn lint:fix --scope=example-a
```

### `yarn test`

Run tests for all packages:

```sh
yarn test
```

Run tests for a single package:

```sh
yarn test --scope=<package-name>
```

For example:

```sh
yarn test --scope=example-a
```
## License

[MIT](LICENSE)
