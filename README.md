# Job scheduler

## Overview

The service allowing scheduling regular jobs. Jobs should be implementend and compiled before the service starts. Schedule and execution details and arguments are stored in the database. Jobs may be added or modified in runtime. E.g. an action can be replicated with different schedules and arguments.

## Motivation

There is a need for service allowing to easily deploy a scheduled task, replicate it with different arguments. The service stores execution log. It is opensource and extensible. More actions will be added in the future. E.g. periodic site avaialbility checker, API diff monitor

## Use cases

1. Replicate existing job to serve multiple users with different arguments
2. Periodic site availability check
3. Check REST API difference and send notification if the resource has changed

## Features

1. Contains set of predefined parametrized actions, the set of actions can be extended.
2. Provides REST API for configuring jobs (add, remove, update)
3. Keep track of invocation history
4. [future] Multitenancy support. Authenticate users and allow to create private set of jobs.

## Implementation details

- [schema](docs/schema.md)