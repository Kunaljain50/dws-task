# Extra Notes Before Production

-   Replace the in-memory repository with a real database and proper
    transactions.
-   Improve error handling and return consistent JSON error responses.
-   Add more tests, especially for concurrent transfers and edge cases.
-   Secure the APIs (authentication/authorization).
-   Add logging, metrics and basic monitoring.
-   Make notifications asynchronous and resilient (retry on failure).
