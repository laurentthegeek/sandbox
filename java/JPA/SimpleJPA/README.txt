// ---------------------------------------------------------------------------
// Single Table Entity
// Object: Person
// Relational: PERSON

@Entity
// @Access(AccessType.{FIELD|PROPERTY}) default according to @ declaration
// @Table(name='PERSON') default <name> ::= <class-name>
class Person
    @Id @GeneratedValue
    Long id

    //@Column(name="name") default name ::= <attr-name>
    String name

    // Temoporal mapped to either SQL DATE or TIMESTAMP
    @Temporal(TemporaleType.{DATE|TIMESTAMP})
    Date createDate

    // Transient attribute (doesn't persist)
    @Transient
    Integer computedAttribute

    // Enumerated
    @Enumerated(EnumType.STRING) ISOCountryCode countryCode


// ---------------------------------------------------------------------------
// Multi Tables Entity
// Object: Person
// Relational: PERSON{id, name} PERSON_NOTES{id,notes] PERSON_IMAGE{id,image}

@Entity
@SecondaryTables({
    @SecondaryTable(name="person_notes"),
    @SecondaryTable(name="person_image")})
class Person
    @Id
    id
    // Basic(fetch=FetchType.LAZY)
    String name
    @Column(table="person_notes")
    String notes
    @Column(table="person_image")
    String image


// ---------------------------------------------------------------------------
// Composite Primary Key (flatten)
// Object: Person
// Relational: PERSON{id, name} PERSON_NOTES{id,notes] PERSON_IMAGE{id,image}

class PersonId
    String firstname
    String lastname

@Entity
@IdClass(PersonId.class)
class Person
    @Id String firstname
    @Id String lastname


// ---------------------------------------------------------------------------
// Composite Primary Key (embeddable)
// Object: Person
// Relational: PERSON{id, name} PERSON_NOTES{id,notes] PERSON_IMAGE{id,image}

@Embeddable
class PersonId
    String firstname
    String lastname

@Entity
class Person
    @EmbeddedId PersonId personId


// ---------------------------------------------------------------------------
// Embeddable
// Object: Customer <>- Address
// Relational: CUSTOMER{ID, Customer-attrs*, Address-attrs* }

@Entity
class Customer {
    @Id id
    @Embedded
    Address address

@Embeddable
@Access(AccessType.PROPERTY) // set mapping regardless of embedders' access type
class Address
    // @Id not allowed


// ---------------------------------------------------------------------------
// OneToOne Unidirectional
// Object: Customer 1->1 Address
// Relational: CUSTOMER{ID, ADDRESS_ID} -> ADDRESS{ID}

@Entity
class Customer
    @Id id
    // default to: @OneToOne @JoinColumn(name="<attr-name> '_' <class-id>")
    //@OneToOne @JoinColumn(name="address_id")
    Address address

@Entity
class Address
    @Id id


// ---------------------------------------------------------------------------
// OneToMany Unidirectional w/ JoinTable (default)
// Object: Order 1->* OrderLine
// Relational: ORDER{ID} <- ORDER_ORDERLINE{ORDER_ID,ORDERLINE_ID[,ORDERLINE_ORDER]} -> ORDERLINE{ID}

@Entity
class Order
    @Id id
    // default to: @OneToMany @JoinTable(name="<class-name> '_' <class-name>")
    //@OneToMany @JoinTable(name="order_orderline")
    //@OrderColumn("orderline_order") default name ::= <class-name> '_ORDER'
    List<OrderLine> orderLines

@Entity
class OrderLine
    @Id id


// ---------------------------------------------------------------------------
// OneToMany Unidirectional w/ JoinColumn
// Object: Order 1->* OrderLine
// Relational: ORDER{ID} <- ORDERLINE{ID, ORDER_ID}

@Entity
class Order
    @Id id
    @OneToMany @JoinColumn
    // @OrderBy sorted when loaded (no impact on mapping)
    List<OrderLine> orderLines

@Entity
class OrderLine
    @Id id


// ---------------------------------------------------------------------------
// ManyToOne Unidirectional
// Object: Book *->1 Editor
// Relational: BOOK{EDITOR_ID} -> EDITOR{ID}

@Entity
class Book
    @ManyToOne @JoinColumn
    Editor editor

@Entity
class Editor
    @Id


// ---------------------------------------------------------------------------
// ManyToMany Bidirectional
// Object: CD *<->* Artist
// Relational: Student {id} <- Student_Course{student_id,course_id} -> Course {id}
// Notes: either side could be the owning side, one side must be set to the
// owner side otherwise ORM provider will assumes both sides are the owner.

@Entity
class Student
    @ManyToMany
    // default to:
    // @JoinTable(name="student_course",
    //     joinColums = @JoinColumn(name="student_id"),
    //     inverseJoinColumns=@JoinColum(name="course_id"))
    List<Course> courses

@Entity Course
    @ManyToMany(mappedBy="courses") // set Student.courses as the owner side
    List<Student> students


// ---------------------------------------------------------------------------
// Inheritance NF compliant (single_table, joined)
// Object: Node
//          +- Root
//          +- Branch
//          +- Leaf
// Relational SINGLE_TABLE:
// NODE { Node-<attr>* + Root-<attr>* + Branch-<attr>* + Leaf<attr>* }
// Relational JOINED:
// NODE {id, Node-attr} ROOT{id,Root-attr} BRANCH{id,Branch-attr} LEAF{id,Leaf-attr}

@Entity
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE|JOINED|TABLE_PER_CLASS)
//@DiscriminatorColumn(name='DTYPE', DiscriminatorType.STRING) (default)
//@DiscriminatorValue("Node") (default to <class-name>)
[abstract] class Node

@Entity
class Root extend Node

@Entity
class Branch extend Node

@Entity
class Leaf extends Node


// ---------------------------------------------------------------------------
// Inheritance denormalize (table_per_class)
// Object: Node
//          +- Root
//          +- Branch
//          +- Leaf
// Relational TABLE_PER_CLASS
// NODE{id, Node-attr} ROOT{id,Node-attr,Root-attr} BRANCH{id,Branch-attr} LEAF{id,Node-attr,Leaf-attr}

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
[abstract] class Node
    String name
    String type

@Entity
@AttributeOverrides({
    @AttributeOverride(name="name", column=@Column(name="node_name")}
class Root extend Node


// ---------------------------------------------------------------------------
// Inheritance special case
//
class Transient // Pure Java class not mapped to persistent entity

@Entity
class Persistent extend Transient // Allowed but Transient still not mapped

@MappedSuperclass
class Node // Class not mapped to an entity or relationship (syntaxic helper only)

@Entity
class Leaf extend Node // Node and Leaf persist
    

// 2.0

// Collection
@ElementCollection
@CollectionTable("tag")
@Column("value")
ArrayList<String> tags;

// Map
@ElementCollection
@CollectionTable(name="track")
@MapKeyColumn("position")
@Column("title")
Map<Integer, String> tracks