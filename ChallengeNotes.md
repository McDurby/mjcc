# My notes during the challenge

## General

-   I modified the Employee implementation:
    -   to avoid the 500 HTTP status codes
    -   `PutMapping` was creating a new record for the same employee id. This cause `GetMapping` to fail with `org.springframework.dao.IncorrectResultSizeDataAccessException returned non unique result`.
-   I avoided `throwing` Exceptions from the `ReportingStructureService` and `CompensationService`. I used `Optional<>` instead.
-   In the `ReportingStructureController` and `CompensationController`, I used a return type of `ResponseEntity` to avoid a 500 HTTP status code.
-   Logged entry and failures
-   Created unit tests for:
    -   `CompensationServiceImplTest`
    -   `ReportingStructureServiceImplTest`

### Returns

Instead of the `ReportingStructure` and `Compensation` having the browser show the following when an invalid employee
`id` is used:

```
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback

There was an unexpected error (type=Internal Server Error, status=500).
Invalid employeeId: 1
```

I used a `ResponseEntity<>` return type for the `ReportingStructureController` and `CompenstationController`.

This permits the source to identify a good response code based on the results. For example `200` response code versus a
`404` response code when an employee is not found by `id`.

## ReportingStructure

JSON Schema:

```json
{
    "type": "object",
    "properties": {
        "employee": {
            "type": "Employee"
        },
        "numberOfReports": {
            "type": "integer",
            "minimum": 0
        }
    }
}
```

### Sample curls

These samples use the "ids" from `employee_database.json`.

If the `id` does not find a employee in the database, `generate-report` **will** respond with a `404`.

```bash
$ curl -v http://localhost:8080/employee/2/generate-report
*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> GET /employee/2/generate-report HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.70.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 404
< Content-Length: 0
<
* Connection #0 to host localhost left intact
```

With the valid employee id for John Lennon, store the output in `john-lennon-report.json`:

```bash
$ curl -v -o john-lennon-report.json http://localhost:8080/employee/16a596ae-edd3-4847-99fe-c4518e82c86f/generate-report
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> GET /employee/16a596ae-edd3-4847-99fe-c4518e82c86f/generate-report HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.70.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 29 Nov 2023 03:41:25 GMT
<
{ [499 bytes data]
100   487    0   487    0     0   9549      0 --:--:-- --:--:-- --:--:--  9740
* Connection #0 to host localhost left intact
```

The `john-lennon-report.json` file:

```json
{
    "employee": {
        "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
        "firstName": "John",
        "lastName": "Lennon",
        "position": "Development Manager",
        "department": "Engineering",
        "directReports": [
            {
                "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            },
            {
                "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            }
        ]
    },
    "numberOfReports": 4
}
```

## Compensation

JSON Schema return value:

```json
{
    "type": "object",
    "properties": {
        "employeeId": {
            "type": "string"
        },
        "employee": {
            "type": "Employee"
        },
        "salary": {
            "type": "number",
            "minimum": 0.0
        },
        "effectiveDate": {
            "type": "string",
            "format": "date-time"
        }
    }
}
```

JSON Schema payload value:

```json
{
    "type": "object",
    "properties": {
        "salary": {
            "type": "number",
            "minimum": 0.0
        },
        "effectiveDate": {
            "type": "string",
            "format": "date-time"
        }
    }
}
```

A sample `Compensation` payload:

```json
{
    "salary": "123456.10",
    "effectiveDate": "2023-12-025T00:00:01+00:00"
}
```

I needed an ID store to the `Compensation`, but during development, I went back and forth between storing the
`Employee` in the `Compensation`.

### Sample curls

These samples use the "ids" from `employee_database.json`.

A request, a correct response before the compensation exists for the employee, `GET` the compensation for a non-existent
employee ID:

```bash
# An employee ID that doesn't exist
$ curl -v http://localhost:8080/employee/1/compensation
*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> GET /employee/1/compensation HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.70.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 404
< Content-Length: 0
<
* Connection #0 to host localhost left intact
```

Create a compensation for John Lennon, using Postman, from the Postman Console:

```bash
POST http://localhost:8080/employee/16a596ae-edd3-4847-99fe-c4518e82c86f/compensation
Request Headers
Content-Type: application/json
User-Agent: PostmanRuntime/7.35.0
Accept: */*
Cache-Control: no-cache
Postman-Token: f7a11a97-ffb3-4207-b666-e4ba9c63e401
Host: localhost:8080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Length: 82
Request Body
{
    "salary": "123456.10",
    "effectiveDate": "2023-12-01T01:00:00+00:00"
}
Response Headers
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 29 Nov 2023 17:29:43 GMT
Response Body
{"employeeId":"16a596ae-edd3-4847-99fe-c4518e82c86f","employee":{"employeeId":"16a596ae-edd3-4847-99fe-c4518e82c86f","firstName":"John","lastName":"Lennon","position":"Senior Development Manager","department":"Engineering","directReports":[{"employeeId":"b7839309-3348-463b-a7e3-5de1c168beb3","firstName":null,"lastName":null,"position":null,"department":null,"directReports":null},{"employeeId":"03aa1462-ffa9-4978-901b-7c001562cf6f","firstName":null,"lastName":null,"position":null,"department":null,"directReports":null}]},"salary":123456.1,"effectiveDate":"2023-12-01T01:00:00.000+0000"}
```

`GET` the compensation for John Lennon:

```bash
$ curl -v -o john-lennon-compensation.json http://localhost:8080/employee/16a596ae-edd3-4847-99fe-c4518e82c86f/compensation
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying ::1:9999...
* Connected to localhost (::1) port 8080 (#0)
> GET /employee/16a596ae-edd3-4847-99fe-c4518e82c86f/compensation HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.70.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 29 Nov 2023 17:33:59 GMT
<
{ [603 bytes data]
100   591    0   591    0     0  13744      0 --:--:-- --:--:-- --:--:-- 13744
* Connection #0 to host localhost left intact
```

The `john-lennon-compensation.json` file:

```json
{
    "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "employee": {
        "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
        "firstName": "John",
        "lastName": "Lennon",
        "position": "Senior Development Manager",
        "department": "Engineering",
        "directReports": [
            {
                "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            },
            {
                "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            }
        ]
    },
    "salary": 123456.1,
    "effectiveDate": "2023-12-01T01:00:00.000+0000"
}
```
