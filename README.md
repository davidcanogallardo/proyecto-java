# proyecto-java

## Control de errores
En la clase main he creado un logger para el fichero log.txt

Luego en la clase ViewController he creado un logger

``` jsx
// Logs
private static Logger logger = Logger.getLogger(ViewController.class.getName());
```

Y  he añadido un try catch en la función principal de la ViewController para que cace todos los errores y si encuentra un error lo guardará en en el log y guardará la información de los DAOs en un .dat
``` jsx
try {
       mainMenu();
   } catch (Exception e) {
       logger.warning(e.toString());
       System.out.println("\n" + e);
       saveDAO();
   }
```

He creado una excepcion llamada StockInsuficientException que puede lanzarse en ``stockGestor()`` en el ViewController cuando se quiere quitar más stock de un producto del que tiene.

```jsx
id = MenuUtils.getExistingId(daoProduct, "ID de un producto: ");
stock = MenuUtils.getInteger("Stock que quitar: ", false);
prod = daoProduct.get(id);
try {
    prod.takeStock(stock);
    System.out.println("Stock quitado!");
} catch (StockInsuficientException e) {
    System.out.println("No puedes quitar tanto stock al producto!");
    // e.printStackTrace();
}
```

Para gestionar el stock he añadido las funciones takeStock y putStock en la clase Product

## Ficheros
### Ejercicio 1
En ``stockGestor()`` en en el ViewController he modificado el menú de añadir stock a un producto para que puedas añadir stock manualmente o a partir de un fichero .dat
```jsx
System.out.println("\nElige una opción:");
System.out.println("[0] Volver");
System.out.println("[1] Añadir stock manualmente");
System.out.println("[2] Añadir stock de una archivo de comandas");
System.out.print("Opción: ");
option = keyboard.nextLine();
switch (option) {
    case "1":
        id = MenuUtils.getExistingId(daoProduct, "ID de un producto: ");
        stock = MenuUtils.getInteger("Stock que añadir: ", false);
        prod = daoProduct.get(id);
        prod.putStock(stock);
        System.out.println("Stock añadido!");
        break;
    case "2":
        String filePath = MenuUtils.getString("Nombre del archivo donde leer la comanda: ", false);
        // System.out.println(filePath);
        if (MenuUtils.fileIsValid(filePath)) {
            putStockFromFile(filePath);
            System.out.println("Stock añadido!");
        }
        break;
}
```
### Ejercicio 2

He creado la funciones save() y load() en los DAOs que guardan/lee de un fichero .dat el HashMap del DAO.

### Ejercicio 1 NO DUAL
He creado una opción en el menú de productos para que pida un ID de producto y el stock para grabarlo en un archivo .dat de un pedido.
En el .dat guardo primero un ID y luego el stock y al leerlo, leo de dos en dos.

## Colecciones
### Ejercicio 1
En teoría el HashMap no ordena, pero como a mí me lo hace no he cambiado nada.

### Ejercicio 2 
En la clase pack he guardado el array de producto como TreeSet.
Y los teléfonos en un LinkedHashSet

### Ejercicio 3 
Para comprobar que el pack no sea repetido he iterado todos los packs del DAO y luego he comparado con .equals() el TreeSet de dicho pack y el del pack que estoy modificando.
```jsx
 System.out.println("Comprobando si el pack es repetido...");
 HashMap<Integer, Product> hm = daoProduct.getMap();
 for (Product prod2 : hm.values()) {
     if (prod2 instanceof Pack) {
         if (prod2.equals(packCopy)) {
             isPackRepeated = true;
             break;
         }
     }
 }
```


### Ejercicio 1 NO DUAL
He añadido un menú en la función listProducts() para que pregunte qué método de ordenación quiere.

```jsx
String option;
List<Product> list = new ArrayList<Product>(daoProduct.getMap().values());

System.out.println("Elige un método de ordenación: ");
System.out.println("[1] Sin orden");
System.out.println("[2] Ordenado por nombre");
System.out.println("[3] Ordenado por precio");
System.out.println("[4] Ordenado por stock");
System.out.print("Opción: ");
option = keyboard.nextLine();

switch (option) {
  case "1":
      printObjects(daoProduct);
      break;
  case "2":
      Collections.sort(list, (m1, m2) -> (m1.getName()).compareTo(m2.getName()));
      printList(list);
      break;
  case "3":
      Collections.sort(list, (m1, m2) -> ((Double) m1.getPrice()).compareTo((Double) m2.getPrice()));
      printList(list);
      break;
  case "4":
      Collections.sort(list, (m1, m2) -> ((Integer) m1.getStock()).compareTo((Integer) m2.getStock()));
      printList(list);
      break;
  default:
      System.out.println("\nTe has equivocado, vuelve a intentarlo.");
      break;
}
```

## Fechas
### Ejercicio 1 
He añadido al contructor de Products los dos campos que pide y he modificado el menú de añadir Products para que pida una fecha válida.
En el menú de Products he añadido una opción para que muestre productos descatalogados a partir de una fecha.
```jsx
String today = LocalDate.now().format(dtf);
date = MenuUtils.getDate("Introduce una fecha [" + today + "]: ", true);
List<Product> list;
if (date == null) {
    date = LocalDate.parse(today, dtf);
}

list = daoProduct.getDiscontinuedProducts(date);
for (Product prod : list) {
    System.out.print("Días de diferencia: ");
    System.out.println(ChronoUnit.DAYS.between(prod.getEndCatalog(), date));
    System.out.println(prod.toString() + "\n");
}
```

### Ejercicio 1 NO DUAL
Para el control de persistencia he creado la Clase Presence con los campos
- id
- fecha 
- hora entrada
- hora salida

La fecha es el día en el que se ficha de entrada y **salida**.

Luego he añadido dos contructores uno sin la hora de salida (para fichar de entrada) y otro con todos los campos.

Luego he creado una DAO de Presence con las funciones add para fichar de entrada y addLeaveTime para ficha de salida.
Fichar entrada:
```jsx
public Presence add(Presence obj) {
   for (Presence presence : hashSet) {
       if (presence.equals(obj)) {
           return null;
       }
   }
   this.hashSet.add(obj);
   return obj;
}
```
En el DAO un trabajador podrá fichar (entrada y salida) varias veces solo si ha fichado de salida o aún no ha fichado. Para comprobar que haya fichado de salida al fichar de entrada itero todos los fichajes y los comparo con equals(). El método equals() de Presence lo he redefinido para que dos Presence sean iguales cuando tienen un mismo id y fecha y tienen la hora de salida en null (es decir solo han fichado de entrada)

```jsx
public boolean equals(Object obj) {
   Presence obj2 = (Presence) obj;
   return this.id == obj2.id && this.date.equals(obj2.date) && this.leaveTime == null;
}
```
Fichar salida:
```jsx
public boolean addLeaveTime(int id) {
 LocalDate today = LocalDate.now();
 for (Presence presence : this.hashSet) {
     if (presence.getId() == id && presence.getDate().compareTo(today) == 0 && presence.getLeaveTime() == null) {
         LocalTime now = LocalTime.now();
         presence.setLeaveTime(now);
         return true;
     }
 }
 return false;
}
```
Para fichar de salida la función devuelve un boolean que indica si se ha podido fichar o no. El criterio para poder fichar es que el usuario que se pasa por parámetro haya fichado de entrada hoy, es decir que el id exista en el DAO, su campo de fecha sea hoy y su campo de hora de salida sea nulo.

En control de presencia se hace en la función clockInOutMenu() de el ViewController

## Internacionalizacion
Para internacionalización he creado una clase estática llamada GenericFormatter. Mediante la función setLocale() (que llamo al principio del código) compruebo el Locale del ordenador y pongo un valor a los atributos de de Locale de la clase:

Atributos para formatar fechas, monedas y números
```jsx
    static Locale lDefault = Locale.getDefault(Category.DISPLAY);
    static Locale lFormat = Locale.getDefault(Category.FORMAT);
    static NumberFormat  nFormatter;
    static NumberFormat cFormatter;
```

Función setLocale():
```jsx
public static void setLocale() {
 if (!lDefault.equals(new Locale("es", "ES")) && !lDefault.equals(new Locale("ca", "ES"))) {
     System.out.println("1");
     lDefault = new Locale("es", "ES");
     System.out.println(lDefault);
     df = DateFormat.getDateInstance(DateFormat.SHORT, lDefault);
 }
 if (!lFormat.equals(new Locale("es", "ES")) && !lFormat.equals(new Locale("ca", "ES"))) {
     System.out.println("2");
     lFormat = new Locale("es", "ES");
     System.out.println(lFormat);
 }
nFormatter = NumberFormat.getNumberInstance(lFormat);
cFormatter = NumberFormat.getCurrencyInstance(lFormat);

 text = ResourceBundle.getBundle("Texts", lDefault);
}
```

Para traducir texto tengo un getter del ResourceBundle disponible para llamar en cualquier parte del código.
```jsx
public static ResourceBundle getText() {
 return text;
}
```
Y, por ejemplo, para formatar un número utilizo esta función:
```jsx
public static String formatNumber(Integer num) {
 if (num != null) {
     return nFormatter.format(num);
 }
 return " ";
}
```

Que puedo llamar en cualquier parte del código, ejemplo:
```jsx
// toString de Product
@Override
public String toString() {
       return "Product [id=" + id + ", name=" + name + ", price=" + GenericFormatter.formatPrice(price)
       + ", startCatalog=" + GenericFormatter.formatDate(startCatalog) + ", endCatalog=" +                                  GenericFormatter.formatDate(endCatalog) + ", stock=" + GenericFormatter.formatNumber(stock) + "]";
}
```
