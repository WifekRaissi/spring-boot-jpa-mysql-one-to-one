# spring-boot-jpa-mysql-one-to-one

Dans le tutorial précédant on a étudié le mapping de la relation One to Many https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-many. 
On continue dans ce tutorial avec la relation One to One entre la table Salarie et une nouvelle table Tache, à un moment donné chaque salarie travaille sur une seule tâche et une tâche est affectée à un seul salarié.
L'architecture de l'application est la suivante:

  ![alt text](https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-one/blob/master/src/main/resources/images/architecture.PNG) 
  
  
  Dans les tutoriaux précédants on créé les tables manuellement. Dans ce tutorial on va laisser Hibernate les créer automatiquement.
  
  
  
