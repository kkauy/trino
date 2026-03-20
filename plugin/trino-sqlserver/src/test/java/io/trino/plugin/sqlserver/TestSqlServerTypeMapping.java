/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.sqlserver;

import io.trino.testing.QueryRunner;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestSqlServerTypeMapping
        extends BaseSqlServerTypeMapping
{
    @Override
    protected QueryRunner createQueryRunner()
            throws Exception
    {
        sqlServer = closeAfterClass(new TestingSqlServer());
        return SqlServerQueryRunner.builder(sqlServer)
                .build();
    }

    @Test
    public void testSqlServerRejectsDecimalNegativeScale()
    {
        assertThatThrownBy(() -> {
            try (
                    Connection connection = sqlServer.createConnection();
                    Statement statement = connection.createStatement()) {
                statement.execute("USE " + sqlServer.getDatabaseName());
                statement.execute("CREATE TABLE dbo.test_decimal_negative_scale (col_0 decimal(5, -2))");
            }
        })
                .hasMessageContaining("Specified scale -2 is invalid");
    }
}
