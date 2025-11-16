# Automatas - Simulador de FA, Analizador lexico y Herramientas de expresiones regulares

> **Proyecto académico para la materia “Lenguajes y Autómatas I”.**  
> Incluye simulación de DFA, NFA y NFA-ε, construcción de autómatas desde expresiones
> regulares, un analizador léxico incremental (Lexical_v1–v10) y una GUI para visualizar FA

## Caracteristicas principales
- **Simulador completo** de DFA, NFA y NFA-ε
- **Carga de autómatas desde JSON** con FAParser  
- **Analizador Léxico modular (v1–v10)** con mejoras graduales:
  - lectura carácter por carácter,
  - eliminación de comentarios,
  - manejo de delimitadores,
  - tabla de símbolos,
  - palabras reservadas,
  - tokens de un solo carácter,
  - soporte para expresiones aritméticas.
- **Conversión de expresiones regulares → NFA-ε**
- **GUI** para visualizar cualquier FA y sus transiciones
- **Estructuras auxiliares**, excepciones personalizadas y utilidades para manipular FA

## Fundamentos Teóricos y Conceptos

Este proyecto se basa en:
- Teoria de Automatas y Lenguajes Formales
- Analisis léxico
- Construccion de Automatas via definiciones formales
- Simulación de DFA, NFA y NFA-ε (closures ε, delta sets, etc

Documento formal con las definiciones utilizadas:  
[**Definiciones Formales**](./DefinicionesFormales.md)

---

## Estructura del Proyecto y Archivos

* **`src/`**: Contiene todo el código fuente del proyecto.
    * `src/core/`: Implementacion de FA (NFA, DFA y NFAE)
    * `src/data_structures/`: Estructuras de datos propias. Por ahora solo uso estructuras de Java
    * `src/exceptions/`: Excepciones personalizadas
    * `src/files/`: FA en JSON y archivos de prueba para el léxico .txt
    * `src/io/`: Manejo de archivos
    * `src/lexicon/`: Lexical_v1–v10, Tabla de Símbolos, AutomataProcessor y ConverStringNumber
    * `src/regulars/`: Construccion de NFAE mediante expresiones regulares
    * `src/ui/`: Interfaz grafica (visualizador de FA unicamente)
    * `src/utils/`: Clases de utilidad
* **`docs/`**: Javadoc generado automaticamente
* **`lib/`**: Librerias externas (gson-2.12.2.jar)

---

## Instalacion y Ejecucion

### Requisitos

* JDK instalado
* IDE (en mi caso es VS Code)

### Clonar el repositorio  
    ```bash
    git clone [https://github.com/Nachus-IHH/Automatas.git](https://github.com/Nachus-IHH/Automatas.git)
    cd [Automatas]
    ```
### Ejecutar
* Puedes ejecutar dentro de core para probar FA
* Lexical_vX dentro de lexicon para probar cualquier version de lexical_vX

---

## Documentación Técnica (Javadoc)
Contiene detalles completos del diseño, clases y métodos

* **Acceso a la Documentación:**
    [**Ver la Documentación Completa en GitHub Pages**](https://nachus-ihh.github.io/Automatas/docs/index.html)
    > *Esta documentación detalla como esta hecho el proyecto*

---

## Extras
* Próximas ideas
* Conversion de NFAE → NFA -> DFA
* Optimización de NFA → DFA
* Minimización de DFA
* Construcción de parse trees para expresiones regulares

## Autor
Nachus-IHH
Este proyecto es de uso académico y educativo