[![Latest version](https://img.shields.io/maven-central/v/software.xdev/spring-data-eclipse-store-migration?logo=apache%20maven)](https://mvnrepository.com/artifact/software.xdev/spring-data-eclipse-store-migration)
[![Build](https://img.shields.io/github/actions/workflow/status/xdev-software/spring-data-eclipse-store-migration/check-build.yml?branch=develop)](https://github.com/xdev-software/spring-data-eclipse-store-migration/actions/workflows/check-build.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xdev-software_spring-data-eclipse-store-migration&metric=alert_status)](https://sonarcloud.io/dashboard?id=xdev-software_spring-data-eclipse-store-migration)

# spring-data-eclipse-store-migration

Migrates your Spring-Data-JPA-project
to [Spring-Data-Eclipse-Store](https://github.com/xdev-software/spring-data-eclipse-store)
through a [OpenRewrite](https://docs.openrewrite.org/)-Recipe.

## Usage

[Usage guide for the latest release](https://github.com/xdev-software/spring-data-eclipse-store-migration/releases/latest#Usage)

> [!CAUTION]
> Since Spring-Data-Eclipse-Store can't handle ```@Query```-Annotations,
> these annotations are getting removed by the Rewrite-Recipe.

## Support

If you need support as soon as possible, and you can't wait for any pull request, feel free to
use [our support](https://xdev.software/en/services/support).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

## Dependencies and Licenses
View the [license of the current project](LICENSE) or the [summary including all dependencies](https://xdev-software.github.io/spring-data-eclipse-store-migration/dependencies)
