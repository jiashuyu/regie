## IDE, Development Tool, Framework

This project uses Gradle as the project management tool, so please load all gradle dependencies before running the code.
Please Use Intellij Idea Ultimate Edition (not Community Edition) with JDK 17 and above to open the project since we are
using SpringBoot. 

## Database Setup with Docker

I'm using docker-compose file to start my PostgresSQL database. So please download and open your Docker Desktop Application 
first (no need to sign in), then in the terminal, type `docker-compose up -d` to pull PostgresSQL database image and start 
the database service. Then run `RegieApplication`, which is the entrypoint of the SpringBoot Application. 

## Configure Data Source in IDE

When you try to configure the data source in Intellij Idea, select PostgresSQL; the username is "postgres"; the password 
is "secret"; the name of the database is "regie". See detailed settings in `application.yml` and database initialization 
SQL script named `database-init.sql` under "/src/main/resource" folder. You can find my screenshots evidence under the
screenshots folder.

## Testing the functions in REGIE system

I wrote unit tests for every function in the system, including model layer, repository layer, service layer, and controller
layer, with a total of 117 passed tests (see unit_test_results.png under screenshots folder). I also wrote a DevRunner
class to test some primary features of this regie system with printed logs while running the `RegieApplication` (See 
dev_runner_logs.png under the screenshot folder).

## Test in Postman

Furthermore, this is a SpringBoot Application with url mappings, so writing a huge command-line main function does not 
actually fit to it. In industries, we usually use Postman to test the urls and this is what I did here. Please see
the screenshot evidence for the Postman tests. After the `RegieApplication` is running, please download and open Postman,
click `import` at the upper-left next to `My Workspace` and upload the `regie.postman_collection.json` file under the 
`postman_tests` folder, and you'll see the tests I wrote and use them to test my REGIE system.
