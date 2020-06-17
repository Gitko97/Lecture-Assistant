# Coding convention rules
## **Brace**  
  **1. Braces are used where optional**  
      Braces are used with if, else, for, do and while statements, even when the body is empty or contains only a single statement.<br/><br/>
  **2. K & R style**  
      Braces follow the Kernighan and Ritchie style ("Egyptian brackets") for nonempty blocks and block-like constructs:
      - No line break before the opening brace.
      - Line break after the opening brace.
      - Line break before the closing brace.
      - Line break after the closing brace, only if that brace terminates a statement or terminates the body of a method, constructor, or named class. For example, there is no line break after the brace if it is followed by else or a comma.  
        ###### Example:   
        if (condition) {  
          ...  
        } else if (condition2) {  
          ...
        } else {  
          ...  
        }<br/><br/>
  **3. Empty blocks: may be concise**  
      - An empty block or block-like construct may be in K & R style. Alternatively, it may be closed immediately after it is opened, with no characters or line break in between ({}), unless it is part of a multi-block statement (one that directly contains multiple blocks: if/else or try/catch/finally).  
        ###### Example: 
        void function() {}  //acceptable  
        void otherFunction() {  //acceptable
        }  
        //not acceptable  
        if (condition) {  
          ...  
        } else {}<br/><br/>
## Block indentation: +2 spaces  
  - Each time a new block or block-like construct is opened, the indent increases by two spaces. When the block ends, the indent returns to the previous indent level. The indent level applies to both code and comments throughout the block.)  
  ###### Example:  
  void funtion() {  
  &nbsp;&nbsp;int a;  
  &nbsp;&nbsp;...  
  }<br/><br/>
## One statement per line  
  - Each statement is followed by a line break.<br/><br/>
## Whitespace  
  **Vertical Whitespace**  
    - Between consecutive members or initializers of a class: fields, constructors, methods, nested classes, static initializers, and instance initializers.  
       - A blank line between two consecutive fields (having no other code between them) is optional. Such blank lines are used as needed to create logical groupings of fields.  
  A single blank line may also appear anywhere it improves readability, for example between statements to organize the code into logical subsections. A blank line before the first member or initializer, or after the last member or initializer of the class, is neither encouraged nor discouraged.<br/><br/>
## Horizontal whitespace  
  **1. Separating any reserved word, such as if, for or catch, from an open parenthesis (() that follows it on that line**<br/><br/>
  **2. Separating any reserved word, such as else or catch, from a closing curly brace (}) that precedes it on that line**<br/><br/>
  **3. Before any open curly brace ({), with two exceptions**  
    - @SomeAnnotation({a, b}) (no space is used)  
    - String[][] x = {{"foo"}}; (no space is required between {{, by item 8 below)<br/><br/>
  **4. On both sides of any binary or ternary operator. This also applies to the following "operator-like" symbols:**  
    - the ampersand in a conjunctive type bound: <T extends Foo & Bar>  
    - the pipe for a catch block that handles multiple exceptions: catch (FooException | BarException e)  
    - the colon (:) in an enhanced for ("foreach") statement  
    - the arrow in a lambda expression: (String str) -> str.length()  
   but not
     - the two colons (::) of a method reference, which is written like Object::toString  
     - the dot separator (.), which is written like object.toString()<br/><br/>
  **5. After ,:; or the closing parenthesis ()) of a cast**<br/><br/>
  **6. On both sides of the double slash (//) that begins an end-of-line comment. Here, multiple spaces are allowed, but not required.**<br/><br/>
  **7. Between the type and variable of a declaration: List<String> list**<br/><br/>
  **8. Optional just inside both braces of an array initializer**
    - new int[] {5, 6} and new int[] { 5, 6 } are both valid<br/><br/>
## Variable declarations<br/><br/>
  **1. One variable per declaration**  
    - Every variable declaration (field or local) declares only one variable: declarations such as int a, b; are not used.  
    **Exception**: Multiple variable declarations are acceptable in the header of a for loop.<br/><br/>
  **2. Declared when needed**  
    - Local variables are not habitually declared at the start of their containing block or block-like construct. Instead, local variables are declared close to the point they are first used (within reason), to minimize their scope. Local variable declarations typically have initializers, or are initialized immediately after declaration.  
<br/><br/>
## Naming  
  **1. Rules by identifier type**<br/><br/>
    **1.1 Package names**  
      - Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example, com.example.deepspace, not com.example.deepSpace or com.example.deep_space.<br/><br/>
    **1.2 Class names**  
      - Class names are written in UpperCamelCase.<br/><br/>
    **1.3 Method names**  
      - Method names are written in lowerCamelCase.<br/><br/>
    **1.4 Constant names**  
      - Constant names use CONSTANT_CASE: all uppercase letters, with each word separated from the next by a single underscore.<br/><br/>
    **1.5 Non-constant field names**  
      - Non-constant field names (static or otherwise) are written in lowerCamelCase.<br/><br/>
    **1.6 Parameter names**  
      - Parameter names are written in lowerCamelCase.  
      One-character parameter names in public methods should be avoided.<br/><br/>
    **1.7 Local variable names**  
      - Local variable names are written in lowerCamelCase.  
      Even when final and immutable, local variables are not considered to be constants, and should not be styled as constants.<br/><br/>
  **2. Camel case: defined**<br/><br/>
    2.1 Convert the phrase to plain ASCII and remove any apostrophes. For example, "Müller's algorithm" might become "Muellers algorithm".<br/><br/>
    2.2 Note that the casing of the original words is almost entirely disregarded. Examples: "XML HTTP request" -> "XmlHttpRequest"

    
  
  
