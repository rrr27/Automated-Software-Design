package Allegory;

import PrologDB.DB;
import PrologDB.Table;
import PrologDB.TableSchema;
import PrologDB.Tuple;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PDD {

  DB db;
  public Table Person;
  public Table Department;
  public Table Division;
  public Table employs_worksin;
  public Table childrenOf_parentsOf;

  public PDD(String fileName) {
     db = DB.readDataBase(fileName);
     Person = db.getTableEH("Person");
     Department = db.getTableEH("Department");
     Division = db.getTableEH("Division");
     employs_worksin = db.getTableEH("employs_worksin");
     childrenOf_parentsOf = db.getTableEH("childrenOf_parentsOf");
  }

  abstract class common <T extends common> {
///commonT
       Table table;

       protected common() { }

       protected common(String tableName, Tuple t) {
          TableSchema ts = t.getSchema();
          if (!ts.getName().equals(tableName)) 
            throw new RuntimeException("assigning non-"+tableName+" table to "+tableName);
          table = new Table(ts).add(t);
       }

       protected common(String tableName, Table tab) {
          if (!tab.getSchema().getName().equals(tableName)) {
             throw new RuntimeException("assigning non-"+tableName+" table to "+tableName);
          }
          table = tab;
        }

       public T select(Predicate<Tuple> p) {
          Table result = table.filter(p);
          return New(result);
        }

       protected abstract T New(Table t);

       public T id() { return New(table); }

       public void print() { table.print(System.out); }

       public void forEach(Consumer<Tuple> action) { table.stream().forEach(t -> action.accept(t)); }

       public T intersect(T tab) { return New(this.table.intersect(tab.table)); }

       public int size() { return this.table.count(); }

       public boolean equals(T tab) { return this.table.equals(tab.table); }
  }

  public class Person extends common<Person> {

       protected Person New(Table t) { return new Person(t); }


       protected Person () { table = Person; }

       protected Person (Table t) { super("Person", t); }


       protected Person (Tuple t) { super("Person", t); }

  }


  public class Department extends common<Department> {

       protected Department New(Table t) { return new Department(t); }


       protected Department () { table = Department; }

       protected Department (Table t) { super("Department", t); }


       protected Department (Tuple t) { super("Department", t); }

  }


  public class Division extends common<Division> {

       protected Division New(Table t) { return new Division(t); }


       protected Division () { table = Division; }

       protected Division (Table t) { super("Division", t); }


       protected Division (Tuple t) { super("Division", t); }

  }


  public class employs_worksin extends common<employs_worksin> {

       protected employs_worksin New(Table t) { return new employs_worksin(t); }


       protected employs_worksin () { table = employs_worksin; }

       protected employs_worksin (Table t) { super("employs_worksin", t); }


       protected employs_worksin (Tuple t) { super("employs_worksin", t); }

  }


  public class childrenOf_parentsOf extends common<childrenOf_parentsOf> {

       protected childrenOf_parentsOf New(Table t) { return new childrenOf_parentsOf(t); }


       protected childrenOf_parentsOf () { table = childrenOf_parentsOf; }

       protected childrenOf_parentsOf (Table t) { super("childrenOf_parentsOf", t); }


       protected childrenOf_parentsOf (Tuple t) { super("childrenOf_parentsOf", t); }

  }

}
