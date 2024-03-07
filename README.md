## IDE, Development Tool, Framework

This project uses Gradle as the project management tool, so please load all gradle dependencies before running the code.
Please Use Intellij Idea Ultimate Edition (not Community Edition) with JDK 17 and above to open the project since we are
using SpringBoot. 

## Database Setup with Docker

I'm using docker-compose file to start my PostgresSQL database. So please open your Docker Desktop Application first 
(no need to sign in), then in the terminal, type `docker-compose up -d` to pull PostgresSQL database image and start the 
database service. Then run `RegieApplication`, which is the entrypoint of the SpringBoot Application. 

## Configure Data Source in IDE

When you try to configure the data source in Intellij Idea, select PostgresSQL; the username is "postgres"; the password 
is "secret"; the name of the database is "regie". See detailed settings in `application.yml` and database initialization 
SQL script named `database-init.sql` under "/src/main/resource" folder. You can find my screenshots evidence in the 
"Deliverable_database_setup" folder.
