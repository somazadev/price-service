# AI usage

## Tool

Claude (Anthropic), used as an AI pair-programming assistant throughout the exercise.

## Approach

AI was used to speed up delivery, not to replace engineering judgement. Every suggestion was
reviewed, adapted to the project and validated by running the test suite before being committed.

The workflow followed three principles:

1. **Generate, then rebuild.** An initial reference implementation was generated and used as a
   blueprint. The final project was then built incrementally from a fresh Spring Initializr project,
   layer by layer, adapting each piece instead of copying the generated solution as a whole.
2. **Validate with tests.** No change was accepted without its corresponding test passing.
3. **Review against requirements.** The assistant was used as a second reviewer to check the
   repository against the statement and the technical review feedback.

## Where AI was used

### Design

- Discussing alternative package structures for the hexagonal architecture and choosing a leaner
  layout for the application layer, while keeping the input and output ports explicit.
- Weighing where the priority resolution rule should live (domain service versus application
  service) and documenting the reasoning.
- Defining how to keep the domain and application layers free of framework dependencies, wiring
  them through a dedicated configuration class.

### Testing

- Enumerating edge cases for the priority algorithm: inclusive date boundaries, overlapping ranges,
  input order independence, priority ties and null candidates.
- Designing the test pyramid: unit tests per layer, a persistence slice test and an end-to-end
  integration test covering the five required scenarios.
- Extending coverage after the technical review: query validation, exception handler and mappers.

### Data

- Converting the provided dataset into `schema.sql` and `data.sql`, normalising its date format.
- Deciding how to handle the audit columns included in the dataset, which are stored but kept out
  of the domain model.

### Troubleshooting

- Adapting test configuration to Spring Boot 4, where several test auto-configuration annotations
  moved to new modules and packages.
- Resolving schema initialisation conflicts when several Spring test contexts share the same
  in-memory database.
- Diagnosing local environment issues, such as the Maven Wrapper running on a JRE instead of a JDK.

### Code review and delivery

- Turning each point of the technical review into a concrete change and commit.
- Reviewing the repository structure, Git history and documentation for consistency, so that the
  README only describes what actually exists in the repository.

## Decisions made during the process

- Rebuilding the project from scratch instead of using the generated solution as is.
- A simplified application package, with the use case implementation directly under `application`.
- Validation owned by `GetApplicablePriceQuery` instead of framework annotations.
- Explicit handling of every exception in `GlobalExceptionHandler`, with a single error format.
- Audit columns of the dataset kept out of the domain model.
- A Git history with one commit per layer, a feature branch and pull requests merged with merge
  commits.
