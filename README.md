# ORC

Open Rewrite Client

## Overview

ORC is a tool for applying and managing source code refactoring recipes. It is a command line tool that can be used to apply recipes to a project.

## Usage

To list recipes, run

```shell
docker run -v $(pwd):/workspace -w /workspace \
    rahulsom/orc:latest \
    list
```

To apply a recipe, run something like this

```shell
docker run -v $(pwd):/workspace -w /workspace \
    rahulsom/orc:latest \
    run \
        --recipe org.openrewrite.java.cleanup.Cleanup
```
