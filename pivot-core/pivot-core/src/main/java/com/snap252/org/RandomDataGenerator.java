package com.snap252.org;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.snap252.org.NumberStatistics.MutableValue;

@NonNullByDefault
public class RandomDataGenerator {
	private static final Random random = new Random();

	@SafeVarargs
	public static Stream<Object[]> getAsStream(int cnt, Function<Random, Object>... generators) {
		return IntStream.range(0, cnt).mapToObj(_ignore -> Stream.of(generators).map(g -> g.apply(random)).toArray());
	}

	public static <@NonNull T> Stream<T> getAsStream(int cnt, Function<Random, T> generators) {
		return IntStream.range(0, cnt).mapToObj(_ignore -> generators.apply(random));
	}

	static <T> T random(@NonNull T[] t) {
		return t[random.nextInt(t.length)];
	}

	public static void main(String[] args) throws Exception {

		// String[] vorname = new String[] { "Hanne", "Herbert", "Karmen",
		// "Klaus", "Helga", "Susanne", "Stefan", "Anne",
		// "Arne" };
		final @NonNull String[] vorname = new @NonNull String[] { "Mia", "Emma", "Hannah", "Hanna", "Sofia", "Sophia",
				"Anna", "Lea", "Leah", "Emilia", "Marie", "Lena", "Leonie", "Emily", "Emilie", "Lina", "Amelie",
				"Sophie", "Sofie", "Lilly", "Lilli", "Luisa", "Louisa", "Johanna", "Laura", "Nele", "Neele", "Lara",
				"Maja", "Maya", "Charlotte", "Clara", "Klara", "Leni", "Sarah", "Sara", "Pia", "Mila", "Alina", "Lisa",
				"Lotta", "Ida", "Julia", "Greta", "Mathilda", "Matilda", "Melina", "Zoe", "Zo�", "Frieda", "Frida",
				"Lia", "Liah", "Lya", "Paula", "Marlene", "Ella", "Emely", "Emelie", "Jana", "Victoria", "Viktoria",
				"Josephine/ Josefine", "Finja", "Finnja", "Isabell", "Isabel", "Isabelle", "Helena", "Isabella",
				"Elisa", "Amy", "Mara", "Marah", "Mira", "Katharina", "Jasmin", "Yasmin", "Stella", "Lucy", "Lucie",
				"Luise", "Louise", "Antonia", "Annika", "Fiona", "Pauline", "Nora", "Eva", "Jule", "Magdalena", "Luna",
				"Merle", "Carla", "Karla", "Maria", "Nina", "Theresa", "Teresa", "Melissa", "Franziska", "Martha",
				"Marta", "Milena", "Chiara", "Kiara", "Ronja", "Carlotta", "Karlotta", "Elena", "Romy", "Mina",
				"Helene", "Selina", "Annabell", "Annabelle", "Paulina", "Vanessa", "Maila", "Mayla", "Anni", "Annie",
				"Anny", "Fabienne", "Zoey", "Sina", "Sinah", "Miriam", "Leila", "Leyla", "Linda", "Aylin", "Eileen",
				"Aileen", "Ayleen", "Samira", "Elina", "Jolina", "Joelina", "Celina", "Elisabeth", "Valentina", "Julie",
				"Kira", "Kyra", "Alissa", "Alyssa", "Olivia", "Jette", "Kim", "Elif", "Aaliyah", "Aliya", "Ela",
				"Lotte", "Anastasia", "Luana", "Hailey", "Haylie", "Lucia", "Lenja", "Lenya", "Rosalie", "Vivien",
				"Vivienne", "Mona", "Lana", "Carolin", "Caroline", "Karoline", "Juna", "Yuna", "Elli", "Elly", "Lynn",
				"Linn", "Diana", "Thea", "Alexandra", "Angelina", "Carolina", "Karolina", "Marla", "Michelle", "Tessa",
				"Tabea", "Celine", "Leticia", "Letizia", "Svea", "Alisa", "Marleen", "Marlen", "Milla", "Amalia",
				"Joleen", "Mariella", "Laila", "Layla", "Liana", "Rebecca", "Alessia", "Kimberly", "Kimberley", "Nala",
				"Nahla", "Nelly", "Nelli", "Alicia", "Annalena", "Emmi", "Emmy", "Aurelia", "Lene", "Christina",
				"Samantha", "Larissa", "Noemi", "Dana", "Ina", "Evelyn", "Evelin", "Eveline", "Maira", "Meyra", "Anne",
				"Natalie", "Nathalie", "Alma", "Amelia", "Giulia", "Lorena", "Fenja", "Zeynep", "Leona", "Tilda",
				"Felicitas", "Liv", "Liliana", "Nisa", "Veronika", "Jara", "Yara", "Xenia", "Amira", "Linea", "Linnea",
				"Medina", "Tuana", "Malia", "Henriette", "Jonna", "Jessika", "Jessica", "Cataleya", "Naila", "Nayla",
				"Valerie", "Alexa", "Carina", "Karina", "Dilara", "Estelle", "Daria", "Joline", "Joeline", "Elise",
				"Helen", "Josie", "Josy", "Rosa", "Azra", "Tamina", "Ava", "Enna", "Bella", "Leana", "Melanie", "Alena",
				"Cheyenne", "Chayenne", "Enie", "Melia", "Meryem", "Esma", "Leandra", "Livia", "Selma", "Malin", "Nela",
				"Ylvi", "Ylvie", "Ashley", "Madita", "Marina", "Marlena", "Janne", "Jill", "Jil", "Maike", "Meike",
				"Rieke", "Amina", "Ayla", "Melinda", "Alea", "Amilia", "Aurora", "Mailin", "Maylin", "Elin", "Enya",
				"Florentine", "Selin", "Valeria", "Annelie", "Heidi", "Malina", "Nicole", "Nika", "Flora", "Holly",
				"Liya", "Josefin", "Josephin", "Lenia", "Milana", "Tamara", "Asya", "Freya", "Lilian", "Lillian",
				"Talia", "Thalia", "Alice", "Mary", "Eliana", "Felina", "Hermine", "Mathea", "Mattea", "Matea", "Sonja",
				"Alisha", "Soraya", "Elaine", "Madeleine", "Jolie", "Ceylin", "Eda", "Svenja", "Jamie", "Kate",
				"Lilith", "Madlen", "Madleen", "Mariam", "Maryam", "Miley", "Saskia", "Tiana", "Abby", "Aleyna", "Ann",
				"Edda", "Jolien", "Adriana", "Cara", "Hedi", "Hedy", "Ilayda", "Jenna", "Miray", "Alia", "Elsa",
				"Esila", "Jennifer", "Alexia", "Ellen", "Felicia", "Janina", "Joana", "Kaja", "Kaya", "Caja", "Liara",
				"Marit", "Juliana", "Juliane", "Lilia", "Smilla", "Talea", "Thalea", "Viola", "Anouk", "Charlotta",
				"Jasmina", "Levke", "Aimee", "Ecrin", "Malea", "Marieke", "Marike", "Naemi", "Adelina", "Mathilde",
				"Melek", "Melisa", "Naomi", "Nike", "Philine", "Shania", "Verena", "Cora", "Felia", "Malou", "Patricia",
				"Bianca", "Bianka", "Claire", "Delia", "Friederike", "Giuliana", "Yagmur", "Cassandra", "Kassandra",
				"Joy", "Loreen", "Sena", "Tara", "Ceyda", "Eslem", "Helin", "Jona", "Jonah", "Lola", "Malena", "Melody",
				"Romina", "Anja", "Fatima", "Zara", "Zehra", "Annemarie", "Cecilia", "Dalia", "Elea", "Ellie", "Katja",
				"Melis", "Stefanie", "Stephanie", "Tina", "Feyza", "Fine", "Josephina", "Josefina", "Vivian", "Adele",
				"Alva", "Eleni", "Eliza", "Enni", "Franka", "Janna", "Maileen", "Mayleen", "Maxi", "Sidney", "Sydney",
				"Ada", "Amara", "Inga", "Leia", "Leya", "Liz", "Lou", "Lydia", "Marisa", "Sila", "Stina", "Tamia",
				"Alara", "Anisa", "Cleo", "Megan", "Nea", "Penelope", "Z�mra", "Beyza", "Charleen", "Femke", "Henrieke",
				"Henrike", "Jamila", "Jenny", "Mirja", "Nila", "Salome", "Sandra", "Alessa", "Christin", "Kristin",
				"Evelina", "Joyce", "Kiana", "Line", "Natalia", "Ria", "Tanja", "Betty", "Davina", "Defne", "Denise",
				"Dila", "Eleonora", "Gloria", "Judith", "Julika", "K�the", "Katrin", "Catrin", "Kathrin", "Laureen",
				"Leonora", "Lisbeth", "Luzi", "Luzie", "Maxima", "Neyla", "Nisanur", "Phoebe", "Ruby", "Sabrina",
				"Vera", "Ziva", "Abigail", "Alya", "Andrea", "Ariana", "Belinay", "Fanny", "Ivy", "Joanna", "Jolin",
				"Lavin", "Maren", "Melike", "Nia", "Nova", "Saphira", "Tia", "Amanda", "Ariane", "Arina", "Dorothea",
				"Emina", "Feline", "Julina", "Lieselotte", "Luca", "Luka", "Mette", "Narin", "Nilay", "Philippa",
				"Polly", "Rafaela", "Raphaela", "Ruth", "Sharon", "Summer", "Clarissa", "Elanur", "Esther", "Isa",
				"Liyana", "Nadine", "Sarina", "Serafina", "Violetta", "Yasmina", "Ylva", "Acelya", "Anita", "Annabella",
				"Ceren", "Damla", "Fatma", "Fina", "Frederike", "Grace", "Lale", "Leevke", "Mareike", "Mieke", "Rahel",
				"Stine", "Timea", "Wiebke", "Alicja", "Anneke" };
		// String[] nachname = new String[] { "Gustavson", "H�ger", "Berner",
		// "Klaus", "Sommermann", "Chand", "Stenzel",
		// "Ovan", "Kotzsack" };
		final @NonNull String[] nachname = new @NonNull String[] { "M�ller", "Schmidt", "Schneider", "Fischer", "Weber",
				"Meyer", "Wagner", "Becker", "Schulz", "Hoffmann", "Sch�fer", "Bauer", "Koch", "Richter", "Klein",
				"Wolf", "Schr�der", "Neumann", "Schwarz", "Braun", "Hofmann", "Zimmermann", "Schmitt", "Hartmann",
				"Kr�ger", "Schmid", "Werner", "Lange", "Schmitz", "Meier", "Krause", "Maier", "Lehmann", "Huber",
				"Mayer", "Herrmann", "K�hler", "Walter", "K�nig", "Schulze", "Fuchs", "Kaiser", "Lang", "Wei�",
				"Peters", "Scholz", "Jung", "M�ller", "Hahn", "Keller", "Vogel", "Schubert", "Roth", "Frank",
				"Friedrich", "Beck", "G�nther", "Berger", "Winkler", "Lorenz", "Baumann", "Schuster", "Kraus", "B�hm",
				"Simon", "Franke", "Albrecht", "Winter", "Ludwig", "Martin", "Kr�mer", "Schumacher", "Vogt", "J�ger",
				"Stein", "Otto", "Gro�", "Sommer", "Haas", "Graf", "Heinrich", "Seidel", "Schreiber", "Ziegler",
				"Brandt", "Kuhn", "Schulte", "Dietrich", "K�hn", "Engel", "Pohl", "Horn", "Sauer", "Arnold", "Thomas",
				"Bergmann", "Busch", "Pfeiffer", "Voigt", "G�tz", "Seifert", "Lindner", "Ernst", "H�bner", "Kramer",
				"Franz", "Beyer", "Wolff", "Peter", "Jansen", "Kern", "Barth", "Wenzel", "Hermann", "Ott", "Paul",
				"Riedel", "Wilhelm", "Hansen", "Nagel", "Grimm", "Lenz", "Ritter", "Bock", "Langer", "Kaufmann", "Mohr",
				"F�rster", "Zimmer", "Haase", "Lutz", "Kruse", "Jahn", "Schumann", "Fiedler", "Thiel", "Hoppe", "Kraft",
				"Michel", "Marx", "Fritz", "Arndt", "Eckert", "Sch�tz", "Walther", "Petersen", "Berg", "Schindler",
				"Kunz", "Reuter", "Sander", "Schilling", "Reinhardt", "Frey", "Ebert", "B�ttcher", "Thiele", "Gruber",
				"Schramm", "Hein", "Bayer", "Fr�hlich", "Vo�", "Herzog", "Hesse", "Maurer", "Rudolph", "Nowak",
				"Geiger", "Beckmann", "Kunze", "Seitz", "Stephan", "B�ttner", "Bender", "G�rtner", "Bachmann",
				"Behrens", "Scherer", "Adam", "Stahl", "Steiner", "Kurz", "Dietz", "Brunner", "Witt", "Moser", "Fink",
				"Ullrich", "Kirchner", "L�ffler", "Heinz", "Schultz", "Ulrich", "Reichert", "Schwab", "Breuer",
				"Gerlach", "Brinkmann", "G�bel", "Blum", "Brand", "Naumann", "Stark", "Wirth", "Schenk", "Binder",
				"K�rner", "Schl�ter", "Rieger", "Urban", "B�hme", "Jakob", "Schr�ter", "Krebs", "Wegner", "Heller",
				"Kopp", "Link", "Wittmann", "Unger", "Reimann", "Ackermann", "Hirsch", "Schiller", "D�ring", "May",
				"Bruns", "Wendt", "Wolter", "Menzel", "Pfeifer", "Sturm", "Buchholz", "Rose", "Mei�ner", "Janssen",
				"Bach", "Engelhardt", "Bischoff", "Bartsch", "John", "Kohl", "Kolb", "M�nch", "Vetter", "Hildebrandt",
				"Renner", "Weiss", "Kiefer", "Rau", "Hinz", "Wilke", "Gebhardt", "Siebert", "Baier", "K�ster", "Rohde",
				"Will", "Fricke", "Freitag", "Nickel", "Reich", "Funk", "Linke", "Keil", "Hennig", "Witte", "Stoll",
				"Schreiner", "Held", "Noack", "Br�ckner", "Decker", "Neubauer", "Westphal", "Heinze", "Baum", "Sch�n",
				"Wimmer", "Marquardt", "Stadler", "Schlegel", "Kremer", "Ahrens", "Hammer", "R�der", "Pieper", "Kirsch",
				"Fuhrmann", "Henning", "Krug", "Popp", "Conrad", "Karl", "Krieger", "Mann", "Wiedemann", "Lemke",
				"Erdmann", "Mertens", "He�", "Esser", "Hanke", "Strau�", "Miller", "Berndt", "Konrad", "Jacob",
				"Philipp", "Metzger", "Henke", "Wiese", "Hauser", "Dittrich", "Albert", "Klose", "Bader", "Herbst",
				"Henkel", "Kr�ger", "Wahl", "Bartels", "Harms", "Fritsch", "Adler", "Gro�mann", "Burger", "Schrader",
				"Probst", "Martens", "Baur", "Burkhardt", "Hess", "Mayr", "Nolte", "Heine", "Kuhlmann", "Klaus",
				"K�hne", "Kluge", "Bernhardt", "Blank", "Hamann", "Steffen", "Brenner", "Rauch", "Reiter", "Preu�",
				"Jost", "Wild", "Hummel", "Beier", "Krau�", "Lindemann", "Herold", "Christ", "Niemann", "Funke",
				"Haupt", "Jan�en", "Vollmer", "Straub", "Strobel", "Wiegand", "Merz", "Haag", "Holz", "Knoll", "Zander",
				"Rausch", "Bode", "Beer", "Betz", "Anders", "Wetzel", "Hartung", "Glaser", "Fleischer", "Rupp",
				"Reichel", "Lohmann", "Diehl", "Jordan", "Eder", "Rothe", "Weis", "Heinemann", "D�rr", "Metz", "Kroll",
				"Freund", "Wegener", "Hohmann", "Gei�ler", "Sch�ler", "Schade", "Raab", "Feldmann", "Zeller", "Neubert",
				"Rapp", "Kessler", "Heck", "Meister", "Stock", "R�mer", "Seiler", "Altmann", "Behrendt", "Jacobs",
				"Mai", "B�r", "Wunderlich", "Sch�tte", "Lauer", "Benz", "Weise", "V�lker", "Sonntag", "B�hler",
				"Gerber", "Kellner", "Bittner", "Schweizer", "Ke�ler", "Hagen", "Wieland", "Born", "Merkel", "Falk",
				"Busse", "Gross", "Eichhorn", "Greiner", "Moritz", "Forster", "Stumpf", "Seidl", "Scharf", "Hentschel",
				"Buck", "Voss", "Hartwig", "Heil", "Eberhardt", "Oswald", "Lechner", "Block", "Heim", "Steffens",
				"Weigel", "Pietsch", "Brandl", "Schott", "Gottschalk", "Bertram", "Ehlers", "Fleischmann", "Albers",
				"Weidner", "Hiller", "Seeger", "Geyer", "J�rgens", "Baumgartner", "Mack", "Schuler", "Appel", "Pape",
				"Dorn", "Wulf", "Opitz", "Wiesner", "Hecht", "Moll", "Gabriel", "Auer", "Engelmann", "Singer",
				"Neuhaus", "Giese", "Sch�tze", "Geisler", "Ruf", "Heuer", "Noll", "Scheffler", "Sauter", "Reimer",
				"Klemm", "Schaller", "Hempel", "Kretschmer", "Runge", "Springer", "Riedl", "Steinbach", "Michels",
				"Barthel", "Pfaff", "Kohler", "Zahn", "Radtke", "Neugebauer", "Hensel", "Winkelmann", "Gebauer",
				"Engels", "Wichmann", "Eichler", "Schnell", "Weller", "Br�ggemann", "Scholl", "Timm", "Siegel", "Heise",
				"R�sch", "B�rger", "Hinrichs", "Stolz", "Walz", "Specht", "Dick", "Geier", "Volk", "Junker", "Prinz",
				"Otte", "Schick", "Klotz", "Haller", "Rother", "Koller", "B�rner", "Thoma", "Drescher", "Kempf",
				"Schirmer", "Faber", "Frenzel", "Uhlig", "Schnabel", "Wirtz", "D�rr", "Kranz", "Kasper", "Hausmann",
				"Hagemann", "Gerhardt", "Lux", "Fries", "Haug", "Endres", "Maas", "Sch�rmann", "Eberle", "Knapp",
				"Eggert", "Brauer", "Finke", "Paulus", "Petzold", "Hauck", "Rath", "Elsner", "Dreyer", "Sievers",
				"Faust", "Dittmann", "Wehner", "Kilian", "Sattler", "Reichelt", "Langner", "Rabe", "Bremer", "Abel",
				"P�tz", "Wittig", "K�hl", "Schober", "Maa�", "Cordes", "Uhl", "Kahl", "Korn", "Harder", "Bernhard",
				"Ullmann", "Thieme", "Klinger", "Bohn", "Biermann", "Vogl", "Sch�tt", "Schaefer", "Nguyen", "Kemper",
				"Knorr", "Michaelis", "Gro�e", "Gerdes", "St�hr", "Hartl", "Lehner", "Mielke", "Eggers", "Schaaf",
				"Sieber", "Melzer", "Behr", "Weiler", "Lippert", "Eckhardt", "H�fer", "Fritzsche", "Helbig", "Theis",
				"Schlosser", "Leonhardt", "Ries", "Reinhold", "Rademacher", "Evers", "Rudolf", "Rost", "Horstmann",
				"Hecker", "Dreher", "Pilz", "Junge", "Ehrhardt", "Matthes", "Klug", "Kunkel", "Steinmetz", "Heitmann",
				"Bahr", "Augustin", "H�hne", "Hering", "Hellmann", "Hildebrand", "Trautmann", "Amann", "Heinrichs",
				"H�hn", "Heimann", "L�ck", "Nitsche", "Sprenger", "Vogler", "Claus", "Jensen", "Blume", "Drews", "Damm",
				"Hofer", "Kurth", "Groth", "Janke", "Heilmann", "Hellwig", "Just", "Wacker", "Huth", "Jahnke",
				"Strauch", "Stenzel", "B�hmer", "Hertel", "Hornung", "G�tze", "Reinhard", "Ruppert", "Lau", "Renz",
				"Sperling", "Teichmann", "Sch�nfeld", "Sp�th", "Hafner", "Alt", "Borchert", "Rehm", "Pohlmann",
				"Pfister", "Zink", "Roos", "Mader", "Wille", "Schroeder", "Heinen", "Lotz", "Balzer", "Schwarze",
				"Westermann", "Ebner", "Krieg", "Schweiger", "Bosch", "Engler", "Schleicher", "Pfeffer", "Gehrke",
				"Kaminski", "Schuh", "Clemens", "Liedtke", "Wessel", "Friedrichs", "Eisele", "Kirchhoff", "Rei�",
				"Brockmann", "Sch�ne", "Frick", "Ulbrich", "Trapp", "R��ler", "Hoyer", "Thomsen", "Scheer", "Wagener",
				"Starke", "Korte", "Baumeister", "Kretzschmar", "Veit", "Grote", "Sachs", "Nitschke", "Bartel",
				"Schwarzer", "Hampel", "Bischof", "Schweitzer", "Seemann", "Grau", "Lehnert", "Orth", "S��", "Loos",
				"Stiller", "Henze", "Lohse", "K�ster", "Baumg�rtner", "Oppermann", "Brendel", "Kirschner", "Sch�ller",
				"Wendel", "Burmeister", "Kastner", "Daniel", "Menke", "Seibert", "Widmann", "David", "Reitz", "K�hnel",
				"Diekmann", "Steinert", "Klatt", "W�rner", "Wolters", "F�rst", "Lampe", "Heckmann", "Wilde", "Buchner",
				"Becher", "Heider", "Grundmann", "Schwabe", "Hager", "Buschmann", "Keck", "M�hlbauer", "Schauer",
				"Petri", "R�hl", "Eckstein", "Schatz", "Kolbe", "Kling", "Knobloch", "Otten", "Muth", "Reinke",
				"Baumgart", "Horst", "Doll", "Kugler", "Gl�ser", "Stange", "Tietz", "Schell", "Br�ning", "Helm",
				"Hacker", "Cremer", "Riemer", "Behnke", "Heyer", "Reiser", "Steinke", "Ostermann", "B�chner", "H�usler",
				"Jacobi", "Heuser", "Obermeier", "Herr", "K�bler", "Spies", "Spindler", "Schmidtke", "Hermes",
				"Kreuzer", "Kock", "Stamm", "Pauli", "Ewald", "Hagedorn", "Kersten", "Weiland", "Resch", "Neu", "Pabst",
				"Kleine", "Gr�n", "Janzen", "Berthold", "Apel", "L�hr", "Jakobs", "Friedl", "Ehlert", "Bastian", "Volz",
				"Fritsche", "Cramer", "Weinert", "Frisch", "Grund", "Wilms", "Scheller", "Enders", "Mahler", "Brandes",
				"Hamm", "Wieczorek", "Burghardt", "Schwartz", "Thies", "Br�ck", "Stern", "Lukas", "Rode", "Hanisch",
				"Lauterbach", "Gehrmann", "Yilmaz", "Adams", "Henn", "Merten", "Gottwald", "Petry", "Gehring", "Hack",
				"Niemeyer", "Backhaus", "Rupprecht", "Heidrich", "Heidenreich", "Mende", "Volkmann", "Hille", "Herz",
				"B�ttger", "Knauer", "K�rber", "Baumgarten", "Bucher", "Schaub", "Michael", "Eckardt", "Lerch", "Jonas",
				"Rahn", "Budde", "R�sler", "Hannemann", "Seidler", "Schiffer", "Sachse", "Ochs", "Brehm", "Hillebrand",
				"Hardt", "Zeidler", "W�st", "K�pper", "Ebeling", "H�lscher", "Gr�newald", "Kowalski", "Thiemann",
				"Reis", "Welsch", "Schultze", "Sailer", "Haack", "Ortmann", "Meurer", "Ebel", "Seibel", "Kellermann",
				"K�hn", "Tiedemann", "Kunert", "Br�uer", "Schaper", "Ehrlich", "Reif", "Aigner", "Wulff", "Berner",
				"Bormann", "Schr�er", "Armbruster", "Eilers", "Raabe", "Fichtner", "Thelen", "Bolz", "Pahl", "Mangold",
				"Scheel", "Kratz", "Hoch", "Backes", "Schuhmacher", "Reinecke", "Z�ller", "Johannsen", "Dieckmann",
				"Drechsler", "Emmerich", "Rauscher", "Post", "Weigand", "Hill", "Andres", "W�chter", "Stratmann",
				"Wallner", "Spengler", "Metzner", "Merk", "Palm", "Hopf", "Dietze", "Kammerer", "Krohn", "Kleinert",
				"Linder", "Henrich", "Morgenstern", "Rogge", "Grube", "Grabowski", "Wurm", "Kummer", "Hoff", "Paulsen",
				"Ertl", "Zorn", "Petermann", "K�ppers", "Buchmann", "Dreier", "Sommerfeld", "B�hr", "Rosenberger",
				"Reichardt", "Reimers", "M�rz", "Heger", "Lenk", "Jaeger", "Hopp", "Habermann", "Boldt", "Schreier",
				"Ewert", "Pusch", "Boos", "Finger", "Christmann", "Weiser", "Wendler", "Maul", "Holzer", "Franzen",
				"Wachter", "Lorenzen", "Siegert", "Hollmann", "Ahlers", "Exner", "Grunwald", "Daum", "Struck",
				"Sch�nberger", "Waldmann", "Kie�ling", "B�scher", "Rein", "Hock", "Holzapfel", "K�stner", "Rasch",
				"L�dtke", "Homann", "Steiger", "Gr�f", "St�cker", "Strobl", "Metzler", "Fleck", "Fey", "H�rmann",
				"Lehr", "Arlt", "R�ckert", "Rohr", "Friese", "Fechner", "Eck", "Tillmann", "Englert", "Klee", "Steger",
				"B�cker", "Fiebig", "L�w", "Hermanns", "Zech", "Borchers", "Weinmann", "Rieck", "Markert", "L�cke",
				"Butz", "Friedel", "Aust", "M�bius", "H�rtel", "Clausen", "Deutsch", "Wessels", "Zeitler", "Heidemann",
				"R�hrig", "Siegmund", "Oertel", "R�diger", "Marschall", "Sch�ffer", "Henschel", "Hunger", "Sell",
				"Pfeil", "Goebel", "Edelmann", "Gerhard", "Rosenthal", "Rosenkranz", "Hain", "Z�llner", "K�nzel",
				"Kleinschmidt", "F�rber", "Schacht", "Schwenk", "R�sner", "B�ck", "T�pfer", "Gr�ner", "Spiegel",
				"Weigl", "Georgi", "Bruhn", "Hubert", "Holzmann", "Drexler", "Steinmann", "Groh", "Schmieder", "Kober",
				"Huhn", "Hinze", "Gebhard", "Zapf", "Lederer", "Birk", "Landgraf", "Raschke", "Leitner", "Sch��ler",
				"Kuhl", "Bosse", "Laux", "Rauh", "Christiansen", "Trost", "Reinert", "Klink", "Schuhmann", "Fabian",
				"Rieder", "Fromm", "Holst", "Bauch", "J�ckel", "Gutmann", "Freese", "Weigelt", "Buhl", "Steinberg",
				"Poppe", "Stier", "Jakobi", "Seibold", "Reiner", "Wortmann", "Luft", "Fa�bender", "Wilken", "Schmelzer",
				"Sch�nherr", "Maus", "Hofmeister", "Heide", "Wilkens", "Wolfram", "Stelzer", "Quast", "Bothe",
				"Lachmann", "Schnitzler", "Gr�ger", "M�cke", "Liebig", "Kreutz", "Nitsch", "Gl�ck", "H�fner", "Kohn",
				"Wegmann", "Olbrich", "V�lkel", "Scheibe", "Herbert", "Erhardt", "Sasse", "Giesen", "Jeske", "L�bke",
				"Reck", "Kleemann", "Frei", "Ro�", "Stricker", "Marks", "Kamp", "Denk", "Obst", "Gl�ckner", "Winkel",
				"Rink", "Reese", "Baron", "Prei�", "Riemann", "Gebert", "Kayser", "H�lsmann", "S�nger", "Meinhardt",
				"Loch", "Storch", "Egger", "Degen", "Dittmar", "Diener", "Salzmann", "Stolle", "Zabel", "Goldmann",
				"Schuller", "H�ppner", "Uhlmann", "Biedermann", "Stegemann", "Manz", "Weidemann", "Mattern", "Hamacher",
				"Kropp", "Sch�nfelder", "P�tzold", "Dahmen", "Welter", "Pelz", "Pelzer", "Schmalz", "Donath", "Eichner",
				"Niedermeier", "Scheuermann", "Dengler", "Stork", "Hirt", "Lamprecht", "Bartl", "Ley", "Timmermann",
				"Strasser", "Kleiner", "Lohr", "Knecht", "Mundt", "Klassen", "Stegmann", "Pohle", "Kiel", "Rommel",
				"M�nster", "Bornemann", "Andresen", "Tiemann", "Stangl", "Knop", "Carl", "Merkle", "Gall", "Schild",
				"Hans", "Bieber", "Jankowski", "Rust", "Neumeier", "Beckers", "Greve", "Engelke", "R�ger", "Zielke",
				"Streit", "Lammers", "Anton", "Abraham", "Gries", "Kempe", "Franken", "Br�utigam", "Nissen", "Janz",
				"Reiche", "Linden", "Ring", "Luther", "Stengel", "Gabler", "Karg", "Weinhold", "Radke", "Liebl",
				"Ecker", "Kopf", "Danner", "Weimer", "Holl", "Welz", "Schl�sser", "Weil", "Jeschke", "Bretschneider",
				"Siewert", "Bartz", "Willmann", "Strecker", "Wrobel", "Jacobsen", "Wieser", "Dombrowski", "Carstens",
				"Schreck", "R�cker", "H�ring", "Mager", "A�mann", "Warnecke", "Benner", "Wenz", "Deppe", "Bollmann",
				"Gerken", "Bott", "Hei�", "Meissner", "Eberl", "Spie�", "Matthies", "Keim", "Hauke", "Erb", "Tischer",
				"Kreis", "Grothe", "Ulbricht", "Senger", "Brandenburg", "Friesen", "Matz", "Vogelsang", "Hofbauer",
				"Manthey", "Arens", "Christoph", "Schult", "Knopp", "Dre�ler", "Brill", "Kuhnert", "Bergner", "Maiwald",
				"Braune", "Hoffmeister", "Aschenbrenner", "Borchardt", "Gast", "Schwan", "Teichert", "Sack", "M�ckel",
				"Brune", "Wende", "Gerke", "Kress", "Kahle", "Gruner", "Spitzer", "Dietl", "Rott", "Oster", "Gerstner",
				"Erler", "Lucas", "Heymann", "Bu�", "Steuer", "Hirschmann", "Geis", "Dunkel", "Tr�ger", "Pauly",
				"Kreutzer", "Ha�", "Hauschild", "Hillmann", "Pfau", "Strack", "Eberhard", "Preis", "Kaya", "Willms",
				"Tietze", "Guth", "Buchwald", "Willems", "R�hl", "Fu�", "Hartig", "W�nsch", "Huck", "Reschke", "Reith",
				"Jentsch", "Speck", "H�u�ler", "Rotter", "Schuldt", "Martini", "Kretschmann", "Geppert", "L�sch",
				"Bloch", "Koslowski", "Georg", "Sahin", "Geist", "Pagel", "Rosenberg", "Frings", "Semmler", "Hilger",
				"G�nter", "Dahm", "Drees", "Hauptmann", "Leopold", "Wunder", "Kre�", "Lipp", "Best", "Wendland",
				"Landwehr", "Melcher", "Gr�fe", "B�uerle", "Laube", "Hauer", "Kaul", "Hackl", "Mahr", "Ludewig", "Feil",
				"Engelbrecht", "Eich", "Grunert", "Veith", "Bruder", "Nitzsche", "Knopf", "Schlichting", "Salomon",
				"Bork", "Hilbert", "Arendt", "Dirks", "Blanke", "Droste", "Strunk", "Traub", "Theobald", "Eger",
				"Krauss", "List", "J�kel", "Zobel", "Carstensen", "Stocker", "Schiemann", "Neufeld", "Czech", "Brauner",
				"Wick", "Peschel", "Rettig", "L�we", "Suhr", "Grewe", "Holler", "Scheid", "Ruhland", "Reindl", "Ritz",
				"Grebe", "Koop", "Esch", "Meixner", "Brock", "Schall", "Rottmann", "Reusch", "Donner", "Fischbach",
				"Kehl", "Diedrich", "B�hler", "Ramm", "Theisen", "Sandner", "Pfl�ger", "Buch", "Hof", "Scherf", "Henne",
				"Feller", "R�del", "Munz", "H�nsel", "Sandmann", "Schwaiger", "Herzig", "Ross", "Sch�nemann", "Dahl",
				"Zeh", "K�hner", "Kasten", "B�cker", "Rickert", "D�rfler", "Euler", "Neumaier", "Menz", "Jobst", "Rie�",
				"Kersting", "Thamm", "Knappe", "Gericke", "Wanner", "Kupfer", "Teuber", "G�rtz", "Wunsch", "Thiede",
				"Kirsten", "Schiele", "Bolte", "Kleber", "Gerhards", "Molitor", "Heid", "H�gele", "Kiesel", "Heckel",
				"Rusch", "Behrend", "Mattes", "Demir", "Abele", "R�ttger", "Johann", "Hug", "Baldauf", "Schoch",
				"Heigl", "Bl�mel", "Dr�ger", "Paschke", "Peschke", "Jungmann", "Hell", "Glas", "Staudt", "Ulmer",
				"Wiedmann", "Schroth", "Reu�", "Flohr", "Dorsch", "Werth", "Reichenbach", "Zenker", "Wohlgemuth",
				"Simons", "Celik", "Steinhauer", "Eickhoff", "Kratzer", "Hasse", "Wiemann", "Walker", "Drewes", "Helms",
				"Nowack", "Montag", "D�rner", "Pesch", "Weimann", "T�rk", "Titze", "Stroh", "H�rner", "Brecht",
				"Althoff", "Barz", "Steinhoff", "Jessen", "Scholze", "Gr�nwald", "Kaden", "Faller", "W�nsche",
				"Reuther", "H�nig", "Mair", "J�rg", "M�hring", "Haak", "Mehl", "Grohmann", "Gerth", "W�lfel", "Dammann",
				"Schupp", "Schad", "Markus", "Hilgers", "Blaschke", "Rosenbaum", "Borgmann", "Leicht", "Eichinger",
				"L�ders", "Frese", "Wrede", "Linde", "H�pfner", "Kube", "Nestler", "Joos", "Penner", "Lieb", "Schaal",
				"Drechsel", "Kessel", "Seiffert", "Bellmann", "Stolze", "Ruff", "Weinberger", "Schuck", "Rummel",
				"Seel", "Sonnenberg", "H�ttner", "Schmidbauer", "Neff", "Klement", "Wittke", "Schmiedel", "Liebscher",
				"Bogner", "Erhard", "Burkert", "Boll", "Stehle", "Krings", "Baumbach", "Sch�ning", "Kohlmann",
				"Schwarzkopf", "Schaffer", "Mehnert", "Kreft", "Schreiter", "Junghans", "Hilpert", "Althaus",
				"Messerschmidt", "Marten", "Haake", "Rech", "B�se", "Panzer", "Schlicht", "H�fler", "K�ppen",
				"Eberlein", "Schillinger", "Falke", "Klos", "Belz", "Michalski", "Borowski", "Pott", "Loose", "Mauer",
				"Kurtz", "Taubert", "Heindl", "Mock", "Krull", "Hammerschmidt", "Hahne", "Bickel", "Rautenberg", "Koll",
				"H�hnel", "G�pfert", "K�chler", "St�ckl", "Goller", "Korb", "S�llner", "Hornig", "Dressel", "Wuttke",
				"Schiefer", "Heinzelmann", "Rank", "Staab", "Wiebe", "Holtz", "Richert", "Frerichs", "Flemming", "Hepp",
				"Both", "Degenhardt", "Lippmann", "Utz", "Kittel", "Eckl", "Yildirim", "Schmied", "K�pke", "Gei�",
				"Dohmen", "Zielinski", "Kautz", "Burmester", "Bluhm", "Krone", "Thiem", "Kindler", "Potthoff",
				"Mueller", "Bell", "Aydin", "Straube", "Klar", "Riegel", "Reichmann", "Kluth", "Dittmer", "Welzel",
				"K�mmel", "Holland", "B�cker", "Imhof", "Lewandowski", "Beckert", "Schreyer", "Ehret", "Ruppel",
				"Kaczmarek", "Wiegmann", "Feige", "Tr�ger", "Buss", "Brugger", "Assmann", "Olschewski", "Meinert",
				"Gundlach", "Zacharias", "Stra�er", "Risse", "Pauls", "Heins", "Stelter", "Speer", "Bier", "Teske",
				"Knebel", "Hufnagel", "Mewes", "Windisch", "Warnke", "Frost", "Volkmer", "Schwind", "Reisinger",
				"Debus", "Bopp", "Schill", "Bley", "Mischke", "B�hnke", "Meinecke", "Flach", "G�nzel", "Klier",
				"Bausch", "Teufel", "Br�hl", "Schulthei�", "Oehler", "Hallmann", "Stich", "Meisel", "Krumm", "H�nel",
				"Rathmann", "Leistner", "Domke", "Sigl", "Wenk", "Weigert", "Laue", "Schlecht", "Reiners", "Krenz",
				"Baer", "Kirstein", "Bu�mann", "Ganz", "R�ssler", "Dietzel", "Karsten", "Sch�fers", "B�ckmann",
				"P�hlmann", "Kampmann", "Yildiz", "Borrmann", "K�hler", "Kettner", "Kollmann", "Platz", "Feist",
				"Weyer", "Oldenburg", "Niehaus", "K�lbl", "Weinzierl", "Gutsche", "Dressler", "Zuber", "Buhr", "Garbe",
				"Swoboda", "Moos", "Kr�ner", "Holzner", "Abt", "Reichl", "Nielsen", "Schanz", "Pollmann", "Hipp",
				"Schimmel", "Thielen", "Schultheis", "Nebel", "Steininger", "J�rgensen", "Zinke", "Behringer", "Wald",
				"Matzke", "Stiegler", "Schmuck", "Dobler", "B�rkle", "Rasche", "Eller", "Bade", "Stenger", "Knuth",
				"Seufert", "Jehle", "Sa�", "Roller", "Taube", "Linnemann", "Neuner", "Neuber", "Mathes", "Fetzer",
				"R�hr", "Knauf", "Holstein", "Haufe", "Fleckenstein", "Risch", "Philippi", "Merker", "Kerber",
				"Weitzel", "Scheuer", "Klenk", "Keppler", "�zdemir", "Ostermeier", "Tesch", "Steck", "Knoop", "Kircher",
				"Ehmann", "Knoblauch", "�zt�rk", "R�hle", "Koopmann", "Leuschner", "Laufer", "Schorn", "Friebe", "Mark",
				"Knabe", "Leber", "Lauber", "St�rmer", "Putz", "N�gele", "Meiners", "Weidlich", "P�schel", "Beermann",
				"Sippel", "Schalk", "Reger", "Heumann", "Stang", "Z�hlke", "Milde", "Kindermann", "Weing�rtner",
				"Harnisch", "Groll", "Waibel", "Grosch", "Priebe", "Wellmann", "Marek", "Freier", "Schie�l", "Kelm",
				"Hetzel", "Langen", "Kusch", "Hild", "Grosser", "Fehr", "Ge�ner", "Bruch", "Bohne", "Duda", "Tewes",
				"Stockmann", "Roscher", "Sievert", "Deckert", "Bethke", "Kirch", "Kraemer", "G�rtler", "Sperber",
				"Valentin", "R�ter", "Lippold", "Beil", "Schorr", "Kind", "Ziemann", "Wiechmann", "Schuchardt", "Kuntz",
				"Burkart", "Holtmann", "Welker", "Leipold", "Kistner", "Kaspar", "Griese", "Schimpf", "Schier",
				"Lenzen", "Polster", "Wichert", "Klumpp", "Hintze", "Staiger", "Gabel", "Balke", "Severin", "Kipp",
				"Dogan", "Hintz", "Goldbach", "Stecher", "Meindl", "Pflug", "Lamm", "Witzel", "Thaler", "Rumpf",
				"Demuth", "Plank", "Pick", "Obermaier", "Kloos", "Deutschmann", "Bongartz", "Biehl", "L�ser",
				"Teschner", "Roloff", "Kahlert", "Lietz", "Distler", "Breitenbach", "Thoms", "Kloss", "Hutter",
				"Gaiser", "Pichler", "Krahl", "Brink", "Spielmann", "Seibt", "Kunath", "St�ber", "Zellner", "Scholten",
				"Reinartz", "Ihle", "Wernicke", "Kirschbaum", "Moldenhauer", "Schuch", "Hansmann", "Burkard", "Back",
				"Bitter", "Licht", "Kapp", "Stracke", "Heinisch", "Grossmann", "Weichert", "Sutter", "Matt",
				"Hielscher", "Clasen", "Adolph", "Steinbrecher", "Kothe", "Hey", "Grill", "Br�mmer", "Voit", "Vater",
				"Fuhr", "Eisenmann", "Storz", "Herwig", "Nordmann", "George", "Kappes", "Beutel", "Stender", "Kl�ckner",
				"Angerer", "Kast", "Fl�gel", "Gold", "Steinberger", "Heyne", "Handke", "Brose", "Stolte", "Lutter",
				"Roll", "Kalb", "Spangenberg", "Ilg", "Behrends", "Daub", "R�ther", "Goldschmidt", "Rinke", "Menge",
				"Greif", "Wehrle", "Riese", "Pries", "Lambrecht", "Gerling", "Michl", "Emrich", "Jentzsch", "Sorg",
				"Hundt", "Kamm", "Holzinger", "Wohlfahrt", "Knittel", "Freyer", "Wedel", "M�nch", "Offermann",
				"Schaarschmidt", "Kaufhold", "Wurster", "Tetzlaff", "Schug", "K�llner", "Pfisterer", "Salewski",
				"Schnelle", "Ertel", "Thom", "Klo�", "Epp", "Friedmann", "Fenske", "Rehberg", "Knaus", "Gr�ber",
				"Wittenberg", "Plate", "Mittag", "Junk", "Strube", "M�der", "K�hne", "B�umer", "Maschke", "Kuschel",
				"Kerner", "Herter", "Stemmer", "N�rnberger", "Nicolai", "Cla�en", "Stefan", "Puls", "Gerner",
				"Radermacher", "Germann", "Flick", "Zwick", "Clau�", "Wiest", "Storm", "Harrer", "L�demann", "K�gel",
				"Kullmann", "Sieg", "Sch�ffler", "Schenkel", "Jahns", "Dierks", "Leder", "Franzke", "Retzlaff",
				"Marschner", "H�lzel", "Weidmann", "Ohm", "Helfrich", "B�ker", "Reiss", "Nau", "Mehlhorn", "Lambert",
				"Herzberg", "Bechtold", "Dahms", "Hannig", "Biller", "Wollny", "Meiser", "Raddatz", "Blessing",
				"Scheidt", "Lennartz", "Kunzmann", "Falkenberg", "Naujoks", "Maa�en", "Kaltenbach", "G�hring", "Arend",
				"K�ppe", "Jochum", "Rist", "Mauch", "Bernard", "Thum", "Oltmanns", "Limmer", "Gunkel", "Wall",
				"Niebuhr", "Leis", "Wedekind", "V�lkl", "Alber", "Weitz", "Reh", "Kerscher", "Ru�", "Hammes",
				"Alexander", "Andreas", "Wassermann", "Ro�mann", "Freudenberg", "Stach", "Klostermann", "Johannes",
				"Heinzmann", "Bertsch", "de Vries", "Greulich", "Piel", "Brosch", "Galle", "Cornelius", "Seubert",
				"Plath", "Dr�ge", "Birkner", "Thome", "Nie�en", "B�umler", "Scheffel", "Sanders", "Klingler", "Eckel",
				"Schlenker", "Spahn", "M�hl", "Heyn", "Grams", "Brummer", "Bacher", "Tischler", "Acker", "Hohl",
				"Diederich", "Knoche", "Sch�bel", "Sch�tzle", "Lind", "Krupp", "Klasen", "Klaas", "Streicher",
				"Scheele", "Heer", "Felber", "Schellenberg", "Heiden", "Haberland", "Rosin", "Holm", "Rump", "B�chler",
				"Plum", "Matheis", "H�rtl", "Frahm", "Meder", "Epple", "G�rlich", "Arslan", "Rehbein", "Freytag",
				"K�tter", "Ferber", "Amend", "Mau", "Neitzel", "Lautenschl�ger", "Dahlke", "Adrian", "Scheck",
				"Reinsch", "Pl�tz", "Wilk", "Wenger", "Kutscher", "Kappel", "Mund", "Mandel", "Wehr", "Menges",
				"Zoller", "Schewe", "Zeiler", "Wehrmann", "Kutz", "H�user", "Faulhaber", "Schunk", "Bast",
				"Sternberg" };

		final List<Person> personen = getAsStream(2000, r -> new Person(random(vorname), random(nachname),
				r.nextInt(60) + 10, random(Geschl.values()), new BigDecimal(r.nextInt(10000)).scaleByPowerOfTen(-2)))
						.collect(Collectors.toList());

		BiBucketParameter<Person> parameter = new BiBucketParameter<Person>(personen)
				.setColFnkt(p -> Character.toUpperCase(p.nachname.charAt(0)))

				.setRowFnkt(p -> p.vorname.charAt(0)

		// ,p -> p.geschlecht
		// ,p -> p.alter / 10

		);

		final BiBucket<Person> biBucket = Timers.printTimer("doing bucket", 20, () -> new BiBucket<Person>(parameter));

		Timers.printTimer("printing", () -> write(personen, biBucket));
	}

	protected static void write(List<Person> personen, BiBucket<Person> biBucket2) {
		try (OutputStream os = new BufferedOutputStream(
				new FileOutputStream("C:\\Users\\Snap252\\Documents\\1.html"))) {
			try (Writer writer = new OutputStreamWriter(os)) {
				writeHtml(biBucket2, writer);
			}
			os.flush();
		} catch (final IOException e) {
			throw new AssertionError();
		}
		Timers.printTimer("just write to memory", 10, () -> {
			try (Writer writer = new StringWriter(1 << 20)) {
				writeHtml(biBucket2, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	protected static void writeHtml(BiBucket<Person> biBucket2, Writer writer) throws IOException {
		// Collector<NumberStatistics<Double>, ?, NumberStatistics<Double>>
		// reducer = NumberStatistics
		// .getReducer((n1, n2) -> n1 + n2);
		Collector<Person, MutableValue<BigDecimal>, NumberStatistics<BigDecimal>> reducer = NumberStatistics
				.getReducer(p -> p.wert, new BigDecimalArithmetics());

		BiConsumer<Writer, NumberStatistics<BigDecimal>> cellWriter = (w, ns) -> {
			if (ns.isNeutralElement()) {
				return;
			}
			try {
				w.write("<div title='");
				w.write(ns.toString());
				w.write("'>");
				w.write(ns.sum.toPlainString());
				w.write("</div>");
			} catch (IOException e) {
				assert false;
				e.printStackTrace();
			}

			// writer.write(ns.sum.toPlainString());
		};
		biBucket2.createX(reducer).writeHtml(writer, cellWriter);
	}

	enum Geschl {
		m, w, unbekannt
	}

	static class Person {
		protected final String vorname;
		protected final String nachname;
		protected final Geschl geschlecht;
		private final int alter;
		private final BigDecimal wert;

		public Person(String vorname, String nachname, int alter, Geschl g, BigDecimal wert) {
			this.vorname = vorname;
			this.nachname = nachname;
			this.alter = alter;
			this.geschlecht = g;
			this.wert = wert;
		}

		@Override
		public String toString() {
			return "Person [vorname=" + vorname + ", nachname=" + nachname + ", geschlecht=" + geschlecht + ", alter="
					+ alter + "]";
		}
	}
}
