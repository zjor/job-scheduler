# Schema

## `JOB_DEFINITIONS`

Contains job definitions, what should be executed, when, what to do with the results.

- `id` - `hashid`
- `user_id` - reserved for multitenancy support
- `action` - `JSONB`; job definition, may be extended in the future to load the source code dynamically
- `schedule` - `JSONB`; `{"type": "cron", "value": "*/5 * * * *"}`
- `arguments` - `JSONB`; map of arguments to be passed to the `action`, added into execution context
- `output` - `JSONB`; definition of output, e.g. send result via HTTP
- `created_at` - `timestamp`

## `JOB_EXECUTION_LOG`

Keeps history of job executions, statuses, execution times, phases.

- `job_id`
- `phase` - `enum`; {ACTION | OUTPUT}
- `status` - {SUCCESS | FAILURE}
- `timestamp` - `timestamp`
- `elapsed_time_ms` - `long`
- `data` - `JSONB`; free form data produced during the execution; e.g. output log or exception stacktrace
