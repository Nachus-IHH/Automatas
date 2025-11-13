# Definiciones Formales
## Automatas

### DFA
### NFA
### NFAE


## Estructuras de datos

### Set (Conjunto)

es una coleccion sin elementos duplicados y sin orden definido

Matematicamente
> S = {x | x pertenece a un dominio y no hay repetidos}

Consideraciones
* Implementa internamente con una tabla hash o un arbol balanceado
### ArrayList

es una secuencia ordenada de elementos con acceso por indice, que puede crecer o reducirse dinamicamente

> L = [a0, a1, a2, ..., an-1]

Consideraciones
* arreglo dinamico con redimensionamiento automatico (doblar capacidad)
* Implementar Iterable<_E> para compatibilidad con los automatas

### HashSet (Conjunto con Hashing)

similar a Set pero usa una funcion hash para almacenar los elementos en cubetas (buckets)
> HashSet = {x | hash(x) define la posicion de almacenamiento}

##### Metodos formales y esenciales
Son los mismos que Set, pero con un comportamiento O(1) promedio
* **add(E element)** inserta si no hay otro con el mismo hash
* **remove(E element)** elimina elemento por hash
* **contains(E element)** comprueba existencia
* **size()** cantidad de elementos
* **clear()** limpia todos los buckets
Consideraciones
* _hashCode()_ para obtener posicion
* _equals()_ para colisiones
* para compatibilidad crear HashMap<_E Object> - como lo hace java

### HashMap (Mapa / Diccionario)

un mapa asocia claves (keys) con valores (values)
> M = { (k1, v1), (k2, v2), ..., (kn, vn),}
> y no hay dos claves iguales (Ki != kj)

##### Metodos formales esenciales
* **put(K key, V value)** inserta o reemplaza un valor asociado a una clave
* **get(K key)** obtener el valor asociado
* **remove(K key)** Elimina el par (clave, valor)
* **containsKey(K key)** Comprueba si existe una clave
* **keySet()** conjunto de todas las claves
* **values()** coleccion de todos los valores
* **size()** cantidad de elementos
* **isEmpty()** comprueba si esta vacio
Consideraciones
* _hashCode()_ para obtener posicion
* _equals()_ para colisiones
* para compatibilidad crear HashMap<_E Object> - como lo hace java

### Stack

estructura donde el ultimo elemento en entrar es el primero en salir
> P = [ x1, x2, ..., xn ]
operaciones
* push(x) inserta al tope
* pop() elimina el tope
* peek() observa el tope

##### Metodos formales esenciales
* **push(E element)** inserta un elemento al tope :void
* **pop()** elimina y retorna el elemento del tope : E
* **peek()** retorna el elemento del tope
* **size()** devuelve el numero de elementos : int
* **isEmpty()** indica si la pila esta vacia : boolean

## Lexico
Automata es un fa
Token es el name del fa
lexema es una instancia/cadena que pertenece a un token