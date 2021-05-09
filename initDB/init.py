#!/usr/bin/python

import argparse
import os
import sys

import psycopg2
import sqlparse


class Constants:
    DATABASE_NAME = "pd_aggregator"
    INIT_SQL_FILE = "sql/init.sql"
    INSERT_DATA_FILE = "sql/update.sql"

    HOST = 'localhost'
    PORT = 5432
    USER_NAME = "pd_aggregator"
    USER_PASSWORD = "pd_aggregator"


def parse_arguments():
    parser = argparse.ArgumentParser(description='Perform database migration script')
    parser.add_argument("--db_host", default=Constants.HOST, help="DB Host")
    parser.add_argument("--db_port", default=Constants.PORT, type=int, help="DB Port")
    parser.add_argument("--db_user_name", default=Constants.USER_NAME, help= "DB User Name")
    parser.add_argument("--db_user_password", default=Constants.USER_PASSWORD, help="DB User Password")
    return parser.parse_args()


def setup_db_connection(args, db_name):
    connection = psycopg2.connect(host=args.db_host, port=args.db_port, user=args.db_user_name,
                                  password=args.db_user_password, database=db_name)
    return connection.cursor(), connection


def execute_scripts_from_file(c, filename):
    with open(filename, 'r') as fd:
        sqlCommands = sqlparse.split(
            sqlparse.format(
                fd.read(),
                strip_comments=True,
                keyword_case='upper').rstrip())

    # Execute every command from the input file
    for command in sqlCommands:
        try:
            c.execute('''{0}'''.format(command))
        except Exception as e:
            error = "Migration script ({0}) execution failed: {1}".format(command, e)
            print(error)
            raise e


# Initial setup of an empty Database
def initialize_database(args):
    cursor, connection = setup_db_connection(args, Constants.DATABASE_NAME)

    # Run Initialization Scripts
    init_sql_full_path = os.path.join(Constants.INIT_SQL_FILE)
    execute_scripts_from_file(cursor, init_sql_full_path)

    connection.commit()
    connection.close()
    print('''INFO: SUCCESS - Database Initialized''')


def insert_data(args):
    cursor, connection = setup_db_connection(args, Constants.DATABASE_NAME)

    # Run Initialization Scripts
    init_sql_full_path = os.path.join(Constants.INSERT_DATA_FILE)
    execute_scripts_from_file(cursor, init_sql_full_path)

    connection.commit()
    connection.close()
    print('''INFO: SUCCESS - Database Inserted''')


def main():
    args = parse_arguments()

    if args:
        try:
            initialize_database(args)
            insert_data(args)
        except psycopg2.Error as e:
            print('Initialization FAILED: Error:{}'.format(e))
            print('Error: pgcode - {0}'.format(e))
            raise

    return 0


if __name__ == "__main__":
    sys.exit(main())
