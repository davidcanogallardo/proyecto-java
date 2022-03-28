# proyecto-java

## Control de errores
He añadido un try catch en la función principal de la vista para que cace todos los errores y si encuentra un error lo guardará en en el log y guardará la información de los DAOs en un .dat
``` jsx
     try {
            mainMenu();
        } catch (Exception e) {
            logger.warning(e.toString());
            System.out.println("\n" + e);
            saveDAO();
        }
```
## Ficheros

## Colecciones

## Fechas

## Internacionalizacion
