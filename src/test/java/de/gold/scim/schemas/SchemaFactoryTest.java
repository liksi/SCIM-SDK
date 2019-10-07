package de.gold.scim.schemas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.gold.scim.constants.ClassPathReferences;
import de.gold.scim.utils.JsonHelper;


/**
 * author Pascal Knueppel <br>
 * created at: 03.10.2019 - 17:06 <br>
 * <br>
 */
class SchemaFactoryTest
{

  /**
   * a unit test schema factory instance
   */
  private SchemaFactory schemaFactory;

  /**
   * initializes the schema factory instance for unit tests
   */
  @BeforeEach
  public void initialize()
  {
    schemaFactory = Assertions.assertDoesNotThrow(SchemaFactory::getUnitTestInstance);
  }

  /**
   * this test will assure that the default schemata will be read correctly from the classpath
   */
  @ParameterizedTest
  @ValueSource(strings = {"urn:ietf:params:scim:schemas:core:2.0:Schema",
                          "urn:ietf:params:scim:schemas:core:2.0:ResourceType",
                          "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig"})
  public void testGetDefaultMetaSchemata(String schemaId)
  {
    Assertions.assertNotNull(schemaFactory.getMetaSchema(schemaId));
  }

  /**
   * this test will assure that the default schemata will be read correctly from the classpath
   */
  @ParameterizedTest
  @ValueSource(strings = {"urn:ietf:params:scim:schemas:core:2.0:User", "urn:ietf:params:scim:schemas:core:2.0:Group"})
  public void testGetDefaultResourceSchemata(String schemaId)
  {
    schemaFactory.registerResourceSchema(JsonHelper.loadJsonDocument(ClassPathReferences.USER_SCHEMA_JSON));
    schemaFactory.registerResourceSchema(JsonHelper.loadJsonDocument(ClassPathReferences.GROUP_SCHEMA_JSON));
    Assertions.assertNotNull(schemaFactory.getResourceSchema(schemaId));
  }

  // @formatter:off
  /**
   * this test will make sure that no senseless meta declarations can be used.
   * a senseless declaration might be something like this:
   * {
   *     "name": "id",
   *     "type": "string",
   *     "required": true,
   *     "mutability": "readOnly",
   *     "returned": "never",
   * }
   * the value is required but a readOnly attribute and it is never returned from the server. This declaration
   * simply makes no sense. The client cannot write to this attribute and will never see it
   */
  // @formatter:on
  @Test
  public void testSenselessAttributeCombinationsInMetaSchemata()
  {
    Assertions.fail("this test must verify that no schema can be used that has senseless declarations");
    // @formatter:off
    // senseless declarations:
    //  {
    //     "required": true,
    //     "mutability": "readOnly",
    //     "returned": "never"
    //  }
    //  {
    //     "required": false,
    //     "mutability": "writeOnly",
    //     "returned": "always"
    //  }
    // @formatter:on
  }
}
