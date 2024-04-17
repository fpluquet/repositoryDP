# Data management

The goal of this project is to create a data management system for a company that has author and articles. The company has a lot of authors and articles, and each author can write multiple articles. The company wants to store the data of authors and articles in a database, into files or only in memory. The company wants to be able to add, update, delete and list authors and articles.

## Design patterns

The project uses the following design patterns:
- Factory pattern to create repositories linked to a specific storage type
- Repository pattern to manage the data of authors and articles
- Composite pattern to manage filters in the repository (SQL or lambda)
- Visitor pattern to produce SQL queries from filters or to evaluate filters on data

## Installation

To install the project, you need to clone the repository and install the last version of JDK.

## Usage

To use the project, go to the root of the project and run the following command:

```bash
javac -d bin -cp lib/* src/**/*.java
```

Then, you can run the project with the following command:

```bash
java -cp bin;lib/* controllers.Main
```

You can change the storage type in the Main class.


## Contributors

- [Pluquet Frédéric](https://fpluquet.be)
