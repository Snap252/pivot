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
				"Lotta", "Ida", "Julia", "Greta", "Mathilda", "Matilda", "Melina", "Zoe", "Zoé", "Frieda", "Frida",
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
				"Alara", "Anisa", "Cleo", "Megan", "Nea", "Penelope", "Zümra", "Beyza", "Charleen", "Femke", "Henrieke",
				"Henrike", "Jamila", "Jenny", "Mirja", "Nila", "Salome", "Sandra", "Alessa", "Christin", "Kristin",
				"Evelina", "Joyce", "Kiana", "Line", "Natalia", "Ria", "Tanja", "Betty", "Davina", "Defne", "Denise",
				"Dila", "Eleonora", "Gloria", "Judith", "Julika", "Käthe", "Katrin", "Catrin", "Kathrin", "Laureen",
				"Leonora", "Lisbeth", "Luzi", "Luzie", "Maxima", "Neyla", "Nisanur", "Phoebe", "Ruby", "Sabrina",
				"Vera", "Ziva", "Abigail", "Alya", "Andrea", "Ariana", "Belinay", "Fanny", "Ivy", "Joanna", "Jolin",
				"Lavin", "Maren", "Melike", "Nia", "Nova", "Saphira", "Tia", "Amanda", "Ariane", "Arina", "Dorothea",
				"Emina", "Feline", "Julina", "Lieselotte", "Luca", "Luka", "Mette", "Narin", "Nilay", "Philippa",
				"Polly", "Rafaela", "Raphaela", "Ruth", "Sharon", "Summer", "Clarissa", "Elanur", "Esther", "Isa",
				"Liyana", "Nadine", "Sarina", "Serafina", "Violetta", "Yasmina", "Ylva", "Acelya", "Anita", "Annabella",
				"Ceren", "Damla", "Fatma", "Fina", "Frederike", "Grace", "Lale", "Leevke", "Mareike", "Mieke", "Rahel",
				"Stine", "Timea", "Wiebke", "Alicja", "Anneke" };
		// String[] nachname = new String[] { "Gustavson", "Häger", "Berner",
		// "Klaus", "Sommermann", "Chand", "Stenzel",
		// "Ovan", "Kotzsack" };
		final @NonNull String[] nachname = new @NonNull String[] { "Müller", "Schmidt", "Schneider", "Fischer", "Weber",
				"Meyer", "Wagner", "Becker", "Schulz", "Hoffmann", "Schäfer", "Bauer", "Koch", "Richter", "Klein",
				"Wolf", "Schröder", "Neumann", "Schwarz", "Braun", "Hofmann", "Zimmermann", "Schmitt", "Hartmann",
				"Krüger", "Schmid", "Werner", "Lange", "Schmitz", "Meier", "Krause", "Maier", "Lehmann", "Huber",
				"Mayer", "Herrmann", "Köhler", "Walter", "König", "Schulze", "Fuchs", "Kaiser", "Lang", "Weiß",
				"Peters", "Scholz", "Jung", "Möller", "Hahn", "Keller", "Vogel", "Schubert", "Roth", "Frank",
				"Friedrich", "Beck", "Günther", "Berger", "Winkler", "Lorenz", "Baumann", "Schuster", "Kraus", "Böhm",
				"Simon", "Franke", "Albrecht", "Winter", "Ludwig", "Martin", "Krämer", "Schumacher", "Vogt", "Jäger",
				"Stein", "Otto", "Groß", "Sommer", "Haas", "Graf", "Heinrich", "Seidel", "Schreiber", "Ziegler",
				"Brandt", "Kuhn", "Schulte", "Dietrich", "Kühn", "Engel", "Pohl", "Horn", "Sauer", "Arnold", "Thomas",
				"Bergmann", "Busch", "Pfeiffer", "Voigt", "Götz", "Seifert", "Lindner", "Ernst", "Hübner", "Kramer",
				"Franz", "Beyer", "Wolff", "Peter", "Jansen", "Kern", "Barth", "Wenzel", "Hermann", "Ott", "Paul",
				"Riedel", "Wilhelm", "Hansen", "Nagel", "Grimm", "Lenz", "Ritter", "Bock", "Langer", "Kaufmann", "Mohr",
				"Förster", "Zimmer", "Haase", "Lutz", "Kruse", "Jahn", "Schumann", "Fiedler", "Thiel", "Hoppe", "Kraft",
				"Michel", "Marx", "Fritz", "Arndt", "Eckert", "Schütz", "Walther", "Petersen", "Berg", "Schindler",
				"Kunz", "Reuter", "Sander", "Schilling", "Reinhardt", "Frey", "Ebert", "Böttcher", "Thiele", "Gruber",
				"Schramm", "Hein", "Bayer", "Fröhlich", "Voß", "Herzog", "Hesse", "Maurer", "Rudolph", "Nowak",
				"Geiger", "Beckmann", "Kunze", "Seitz", "Stephan", "Büttner", "Bender", "Gärtner", "Bachmann",
				"Behrens", "Scherer", "Adam", "Stahl", "Steiner", "Kurz", "Dietz", "Brunner", "Witt", "Moser", "Fink",
				"Ullrich", "Kirchner", "Löffler", "Heinz", "Schultz", "Ulrich", "Reichert", "Schwab", "Breuer",
				"Gerlach", "Brinkmann", "Göbel", "Blum", "Brand", "Naumann", "Stark", "Wirth", "Schenk", "Binder",
				"Körner", "Schlüter", "Rieger", "Urban", "Böhme", "Jakob", "Schröter", "Krebs", "Wegner", "Heller",
				"Kopp", "Link", "Wittmann", "Unger", "Reimann", "Ackermann", "Hirsch", "Schiller", "Döring", "May",
				"Bruns", "Wendt", "Wolter", "Menzel", "Pfeifer", "Sturm", "Buchholz", "Rose", "Meißner", "Janssen",
				"Bach", "Engelhardt", "Bischoff", "Bartsch", "John", "Kohl", "Kolb", "Münch", "Vetter", "Hildebrandt",
				"Renner", "Weiss", "Kiefer", "Rau", "Hinz", "Wilke", "Gebhardt", "Siebert", "Baier", "Köster", "Rohde",
				"Will", "Fricke", "Freitag", "Nickel", "Reich", "Funk", "Linke", "Keil", "Hennig", "Witte", "Stoll",
				"Schreiner", "Held", "Noack", "Brückner", "Decker", "Neubauer", "Westphal", "Heinze", "Baum", "Schön",
				"Wimmer", "Marquardt", "Stadler", "Schlegel", "Kremer", "Ahrens", "Hammer", "Röder", "Pieper", "Kirsch",
				"Fuhrmann", "Henning", "Krug", "Popp", "Conrad", "Karl", "Krieger", "Mann", "Wiedemann", "Lemke",
				"Erdmann", "Mertens", "Heß", "Esser", "Hanke", "Strauß", "Miller", "Berndt", "Konrad", "Jacob",
				"Philipp", "Metzger", "Henke", "Wiese", "Hauser", "Dittrich", "Albert", "Klose", "Bader", "Herbst",
				"Henkel", "Kröger", "Wahl", "Bartels", "Harms", "Fritsch", "Adler", "Großmann", "Burger", "Schrader",
				"Probst", "Martens", "Baur", "Burkhardt", "Hess", "Mayr", "Nolte", "Heine", "Kuhlmann", "Klaus",
				"Kühne", "Kluge", "Bernhardt", "Blank", "Hamann", "Steffen", "Brenner", "Rauch", "Reiter", "Preuß",
				"Jost", "Wild", "Hummel", "Beier", "Krauß", "Lindemann", "Herold", "Christ", "Niemann", "Funke",
				"Haupt", "Janßen", "Vollmer", "Straub", "Strobel", "Wiegand", "Merz", "Haag", "Holz", "Knoll", "Zander",
				"Rausch", "Bode", "Beer", "Betz", "Anders", "Wetzel", "Hartung", "Glaser", "Fleischer", "Rupp",
				"Reichel", "Lohmann", "Diehl", "Jordan", "Eder", "Rothe", "Weis", "Heinemann", "Dörr", "Metz", "Kroll",
				"Freund", "Wegener", "Hohmann", "Geißler", "Schüler", "Schade", "Raab", "Feldmann", "Zeller", "Neubert",
				"Rapp", "Kessler", "Heck", "Meister", "Stock", "Römer", "Seiler", "Altmann", "Behrendt", "Jacobs",
				"Mai", "Bär", "Wunderlich", "Schütte", "Lauer", "Benz", "Weise", "Völker", "Sonntag", "Bühler",
				"Gerber", "Kellner", "Bittner", "Schweizer", "Keßler", "Hagen", "Wieland", "Born", "Merkel", "Falk",
				"Busse", "Gross", "Eichhorn", "Greiner", "Moritz", "Forster", "Stumpf", "Seidl", "Scharf", "Hentschel",
				"Buck", "Voss", "Hartwig", "Heil", "Eberhardt", "Oswald", "Lechner", "Block", "Heim", "Steffens",
				"Weigel", "Pietsch", "Brandl", "Schott", "Gottschalk", "Bertram", "Ehlers", "Fleischmann", "Albers",
				"Weidner", "Hiller", "Seeger", "Geyer", "Jürgens", "Baumgartner", "Mack", "Schuler", "Appel", "Pape",
				"Dorn", "Wulf", "Opitz", "Wiesner", "Hecht", "Moll", "Gabriel", "Auer", "Engelmann", "Singer",
				"Neuhaus", "Giese", "Schütze", "Geisler", "Ruf", "Heuer", "Noll", "Scheffler", "Sauter", "Reimer",
				"Klemm", "Schaller", "Hempel", "Kretschmer", "Runge", "Springer", "Riedl", "Steinbach", "Michels",
				"Barthel", "Pfaff", "Kohler", "Zahn", "Radtke", "Neugebauer", "Hensel", "Winkelmann", "Gebauer",
				"Engels", "Wichmann", "Eichler", "Schnell", "Weller", "Brüggemann", "Scholl", "Timm", "Siegel", "Heise",
				"Rösch", "Bürger", "Hinrichs", "Stolz", "Walz", "Specht", "Dick", "Geier", "Volk", "Junker", "Prinz",
				"Otte", "Schick", "Klotz", "Haller", "Rother", "Koller", "Börner", "Thoma", "Drescher", "Kempf",
				"Schirmer", "Faber", "Frenzel", "Uhlig", "Schnabel", "Wirtz", "Dürr", "Kranz", "Kasper", "Hausmann",
				"Hagemann", "Gerhardt", "Lux", "Fries", "Haug", "Endres", "Maas", "Schürmann", "Eberle", "Knapp",
				"Eggert", "Brauer", "Finke", "Paulus", "Petzold", "Hauck", "Rath", "Elsner", "Dreyer", "Sievers",
				"Faust", "Dittmann", "Wehner", "Kilian", "Sattler", "Reichelt", "Langner", "Rabe", "Bremer", "Abel",
				"Pütz", "Wittig", "Kühl", "Schober", "Maaß", "Cordes", "Uhl", "Kahl", "Korn", "Harder", "Bernhard",
				"Ullmann", "Thieme", "Klinger", "Bohn", "Biermann", "Vogl", "Schütt", "Schaefer", "Nguyen", "Kemper",
				"Knorr", "Michaelis", "Große", "Gerdes", "Stöhr", "Hartl", "Lehner", "Mielke", "Eggers", "Schaaf",
				"Sieber", "Melzer", "Behr", "Weiler", "Lippert", "Eckhardt", "Höfer", "Fritzsche", "Helbig", "Theis",
				"Schlosser", "Leonhardt", "Ries", "Reinhold", "Rademacher", "Evers", "Rudolf", "Rost", "Horstmann",
				"Hecker", "Dreher", "Pilz", "Junge", "Ehrhardt", "Matthes", "Klug", "Kunkel", "Steinmetz", "Heitmann",
				"Bahr", "Augustin", "Höhne", "Hering", "Hellmann", "Hildebrand", "Trautmann", "Amann", "Heinrichs",
				"Höhn", "Heimann", "Lück", "Nitsche", "Sprenger", "Vogler", "Claus", "Jensen", "Blume", "Drews", "Damm",
				"Hofer", "Kurth", "Groth", "Janke", "Heilmann", "Hellwig", "Just", "Wacker", "Huth", "Jahnke",
				"Strauch", "Stenzel", "Böhmer", "Hertel", "Hornung", "Götze", "Reinhard", "Ruppert", "Lau", "Renz",
				"Sperling", "Teichmann", "Schönfeld", "Späth", "Hafner", "Alt", "Borchert", "Rehm", "Pohlmann",
				"Pfister", "Zink", "Roos", "Mader", "Wille", "Schroeder", "Heinen", "Lotz", "Balzer", "Schwarze",
				"Westermann", "Ebner", "Krieg", "Schweiger", "Bosch", "Engler", "Schleicher", "Pfeffer", "Gehrke",
				"Kaminski", "Schuh", "Clemens", "Liedtke", "Wessel", "Friedrichs", "Eisele", "Kirchhoff", "Reiß",
				"Brockmann", "Schöne", "Frick", "Ulbrich", "Trapp", "Rößler", "Hoyer", "Thomsen", "Scheer", "Wagener",
				"Starke", "Korte", "Baumeister", "Kretzschmar", "Veit", "Grote", "Sachs", "Nitschke", "Bartel",
				"Schwarzer", "Hampel", "Bischof", "Schweitzer", "Seemann", "Grau", "Lehnert", "Orth", "Süß", "Loos",
				"Stiller", "Henze", "Lohse", "Küster", "Baumgärtner", "Oppermann", "Brendel", "Kirschner", "Schüller",
				"Wendel", "Burmeister", "Kastner", "Daniel", "Menke", "Seibert", "Widmann", "David", "Reitz", "Kühnel",
				"Diekmann", "Steinert", "Klatt", "Wörner", "Wolters", "Fürst", "Lampe", "Heckmann", "Wilde", "Buchner",
				"Becher", "Heider", "Grundmann", "Schwabe", "Hager", "Buschmann", "Keck", "Mühlbauer", "Schauer",
				"Petri", "Rühl", "Eckstein", "Schatz", "Kolbe", "Kling", "Knobloch", "Otten", "Muth", "Reinke",
				"Baumgart", "Horst", "Doll", "Kugler", "Gläser", "Stange", "Tietz", "Schell", "Brüning", "Helm",
				"Hacker", "Cremer", "Riemer", "Behnke", "Heyer", "Reiser", "Steinke", "Ostermann", "Büchner", "Häusler",
				"Jacobi", "Heuser", "Obermeier", "Herr", "Kübler", "Spies", "Spindler", "Schmidtke", "Hermes",
				"Kreuzer", "Kock", "Stamm", "Pauli", "Ewald", "Hagedorn", "Kersten", "Weiland", "Resch", "Neu", "Pabst",
				"Kleine", "Grün", "Janzen", "Berthold", "Apel", "Löhr", "Jakobs", "Friedl", "Ehlert", "Bastian", "Volz",
				"Fritsche", "Cramer", "Weinert", "Frisch", "Grund", "Wilms", "Scheller", "Enders", "Mahler", "Brandes",
				"Hamm", "Wieczorek", "Burghardt", "Schwartz", "Thies", "Brück", "Stern", "Lukas", "Rode", "Hanisch",
				"Lauterbach", "Gehrmann", "Yilmaz", "Adams", "Henn", "Merten", "Gottwald", "Petry", "Gehring", "Hack",
				"Niemeyer", "Backhaus", "Rupprecht", "Heidrich", "Heidenreich", "Mende", "Volkmann", "Hille", "Herz",
				"Böttger", "Knauer", "Körber", "Baumgarten", "Bucher", "Schaub", "Michael", "Eckardt", "Lerch", "Jonas",
				"Rahn", "Budde", "Rösler", "Hannemann", "Seidler", "Schiffer", "Sachse", "Ochs", "Brehm", "Hillebrand",
				"Hardt", "Zeidler", "Wüst", "Küpper", "Ebeling", "Hölscher", "Grünewald", "Kowalski", "Thiemann",
				"Reis", "Welsch", "Schultze", "Sailer", "Haack", "Ortmann", "Meurer", "Ebel", "Seibel", "Kellermann",
				"Köhn", "Tiedemann", "Kunert", "Bräuer", "Schaper", "Ehrlich", "Reif", "Aigner", "Wulff", "Berner",
				"Bormann", "Schröer", "Armbruster", "Eilers", "Raabe", "Fichtner", "Thelen", "Bolz", "Pahl", "Mangold",
				"Scheel", "Kratz", "Hoch", "Backes", "Schuhmacher", "Reinecke", "Zöller", "Johannsen", "Dieckmann",
				"Drechsler", "Emmerich", "Rauscher", "Post", "Weigand", "Hill", "Andres", "Wächter", "Stratmann",
				"Wallner", "Spengler", "Metzner", "Merk", "Palm", "Hopf", "Dietze", "Kammerer", "Krohn", "Kleinert",
				"Linder", "Henrich", "Morgenstern", "Rogge", "Grube", "Grabowski", "Wurm", "Kummer", "Hoff", "Paulsen",
				"Ertl", "Zorn", "Petermann", "Küppers", "Buchmann", "Dreier", "Sommerfeld", "Bähr", "Rosenberger",
				"Reichardt", "Reimers", "März", "Heger", "Lenk", "Jaeger", "Hopp", "Habermann", "Boldt", "Schreier",
				"Ewert", "Pusch", "Boos", "Finger", "Christmann", "Weiser", "Wendler", "Maul", "Holzer", "Franzen",
				"Wachter", "Lorenzen", "Siegert", "Hollmann", "Ahlers", "Exner", "Grunwald", "Daum", "Struck",
				"Schönberger", "Waldmann", "Kießling", "Büscher", "Rein", "Hock", "Holzapfel", "Kästner", "Rasch",
				"Lüdtke", "Homann", "Steiger", "Gräf", "Stöcker", "Strobl", "Metzler", "Fleck", "Fey", "Hörmann",
				"Lehr", "Arlt", "Rückert", "Rohr", "Friese", "Fechner", "Eck", "Tillmann", "Englert", "Klee", "Steger",
				"Bäcker", "Fiebig", "Löw", "Hermanns", "Zech", "Borchers", "Weinmann", "Rieck", "Markert", "Lücke",
				"Butz", "Friedel", "Aust", "Möbius", "Härtel", "Clausen", "Deutsch", "Wessels", "Zeitler", "Heidemann",
				"Röhrig", "Siegmund", "Oertel", "Rüdiger", "Marschall", "Schäffer", "Henschel", "Hunger", "Sell",
				"Pfeil", "Goebel", "Edelmann", "Gerhard", "Rosenthal", "Rosenkranz", "Hain", "Zöllner", "Künzel",
				"Kleinschmidt", "Färber", "Schacht", "Schwenk", "Rösner", "Böck", "Töpfer", "Grüner", "Spiegel",
				"Weigl", "Georgi", "Bruhn", "Hubert", "Holzmann", "Drexler", "Steinmann", "Groh", "Schmieder", "Kober",
				"Huhn", "Hinze", "Gebhard", "Zapf", "Lederer", "Birk", "Landgraf", "Raschke", "Leitner", "Schüßler",
				"Kuhl", "Bosse", "Laux", "Rauh", "Christiansen", "Trost", "Reinert", "Klink", "Schuhmann", "Fabian",
				"Rieder", "Fromm", "Holst", "Bauch", "Jäckel", "Gutmann", "Freese", "Weigelt", "Buhl", "Steinberg",
				"Poppe", "Stier", "Jakobi", "Seibold", "Reiner", "Wortmann", "Luft", "Faßbender", "Wilken", "Schmelzer",
				"Schönherr", "Maus", "Hofmeister", "Heide", "Wilkens", "Wolfram", "Stelzer", "Quast", "Bothe",
				"Lachmann", "Schnitzler", "Gröger", "Mücke", "Liebig", "Kreutz", "Nitsch", "Glück", "Häfner", "Kohn",
				"Wegmann", "Olbrich", "Völkel", "Scheibe", "Herbert", "Erhardt", "Sasse", "Giesen", "Jeske", "Lübke",
				"Reck", "Kleemann", "Frei", "Roß", "Stricker", "Marks", "Kamp", "Denk", "Obst", "Glöckner", "Winkel",
				"Rink", "Reese", "Baron", "Preiß", "Riemann", "Gebert", "Kayser", "Hülsmann", "Sänger", "Meinhardt",
				"Loch", "Storch", "Egger", "Degen", "Dittmar", "Diener", "Salzmann", "Stolle", "Zabel", "Goldmann",
				"Schuller", "Höppner", "Uhlmann", "Biedermann", "Stegemann", "Manz", "Weidemann", "Mattern", "Hamacher",
				"Kropp", "Schönfelder", "Pätzold", "Dahmen", "Welter", "Pelz", "Pelzer", "Schmalz", "Donath", "Eichner",
				"Niedermeier", "Scheuermann", "Dengler", "Stork", "Hirt", "Lamprecht", "Bartl", "Ley", "Timmermann",
				"Strasser", "Kleiner", "Lohr", "Knecht", "Mundt", "Klassen", "Stegmann", "Pohle", "Kiel", "Rommel",
				"Münster", "Bornemann", "Andresen", "Tiemann", "Stangl", "Knop", "Carl", "Merkle", "Gall", "Schild",
				"Hans", "Bieber", "Jankowski", "Rust", "Neumeier", "Beckers", "Greve", "Engelke", "Rüger", "Zielke",
				"Streit", "Lammers", "Anton", "Abraham", "Gries", "Kempe", "Franken", "Bräutigam", "Nissen", "Janz",
				"Reiche", "Linden", "Ring", "Luther", "Stengel", "Gabler", "Karg", "Weinhold", "Radke", "Liebl",
				"Ecker", "Kopf", "Danner", "Weimer", "Holl", "Welz", "Schlösser", "Weil", "Jeschke", "Bretschneider",
				"Siewert", "Bartz", "Willmann", "Strecker", "Wrobel", "Jacobsen", "Wieser", "Dombrowski", "Carstens",
				"Schreck", "Rücker", "Häring", "Mager", "Aßmann", "Warnecke", "Benner", "Wenz", "Deppe", "Bollmann",
				"Gerken", "Bott", "Heiß", "Meissner", "Eberl", "Spieß", "Matthies", "Keim", "Hauke", "Erb", "Tischer",
				"Kreis", "Grothe", "Ulbricht", "Senger", "Brandenburg", "Friesen", "Matz", "Vogelsang", "Hofbauer",
				"Manthey", "Arens", "Christoph", "Schult", "Knopp", "Dreßler", "Brill", "Kuhnert", "Bergner", "Maiwald",
				"Braune", "Hoffmeister", "Aschenbrenner", "Borchardt", "Gast", "Schwan", "Teichert", "Sack", "Möckel",
				"Brune", "Wende", "Gerke", "Kress", "Kahle", "Gruner", "Spitzer", "Dietl", "Rott", "Oster", "Gerstner",
				"Erler", "Lucas", "Heymann", "Buß", "Steuer", "Hirschmann", "Geis", "Dunkel", "Tröger", "Pauly",
				"Kreutzer", "Haß", "Hauschild", "Hillmann", "Pfau", "Strack", "Eberhard", "Preis", "Kaya", "Willms",
				"Tietze", "Guth", "Buchwald", "Willems", "Röhl", "Fuß", "Hartig", "Wünsch", "Huck", "Reschke", "Reith",
				"Jentsch", "Speck", "Häußler", "Rotter", "Schuldt", "Martini", "Kretschmann", "Geppert", "Lösch",
				"Bloch", "Koslowski", "Georg", "Sahin", "Geist", "Pagel", "Rosenberg", "Frings", "Semmler", "Hilger",
				"Günter", "Dahm", "Drees", "Hauptmann", "Leopold", "Wunder", "Kreß", "Lipp", "Best", "Wendland",
				"Landwehr", "Melcher", "Gräfe", "Bäuerle", "Laube", "Hauer", "Kaul", "Hackl", "Mahr", "Ludewig", "Feil",
				"Engelbrecht", "Eich", "Grunert", "Veith", "Bruder", "Nitzsche", "Knopf", "Schlichting", "Salomon",
				"Bork", "Hilbert", "Arendt", "Dirks", "Blanke", "Droste", "Strunk", "Traub", "Theobald", "Eger",
				"Krauss", "List", "Jäkel", "Zobel", "Carstensen", "Stocker", "Schiemann", "Neufeld", "Czech", "Brauner",
				"Wick", "Peschel", "Rettig", "Löwe", "Suhr", "Grewe", "Holler", "Scheid", "Ruhland", "Reindl", "Ritz",
				"Grebe", "Koop", "Esch", "Meixner", "Brock", "Schall", "Rottmann", "Reusch", "Donner", "Fischbach",
				"Kehl", "Diedrich", "Böhler", "Ramm", "Theisen", "Sandner", "Pflüger", "Buch", "Hof", "Scherf", "Henne",
				"Feller", "Rödel", "Munz", "Hänsel", "Sandmann", "Schwaiger", "Herzig", "Ross", "Schünemann", "Dahl",
				"Zeh", "Kühner", "Kasten", "Böcker", "Rickert", "Dörfler", "Euler", "Neumaier", "Menz", "Jobst", "Rieß",
				"Kersting", "Thamm", "Knappe", "Gericke", "Wanner", "Kupfer", "Teuber", "Görtz", "Wunsch", "Thiede",
				"Kirsten", "Schiele", "Bolte", "Kleber", "Gerhards", "Molitor", "Heid", "Hägele", "Kiesel", "Heckel",
				"Rusch", "Behrend", "Mattes", "Demir", "Abele", "Röttger", "Johann", "Hug", "Baldauf", "Schoch",
				"Heigl", "Blümel", "Dräger", "Paschke", "Peschke", "Jungmann", "Hell", "Glas", "Staudt", "Ulmer",
				"Wiedmann", "Schroth", "Reuß", "Flohr", "Dorsch", "Werth", "Reichenbach", "Zenker", "Wohlgemuth",
				"Simons", "Celik", "Steinhauer", "Eickhoff", "Kratzer", "Hasse", "Wiemann", "Walker", "Drewes", "Helms",
				"Nowack", "Montag", "Dörner", "Pesch", "Weimann", "Türk", "Titze", "Stroh", "Hörner", "Brecht",
				"Althoff", "Barz", "Steinhoff", "Jessen", "Scholze", "Grünwald", "Kaden", "Faller", "Wünsche",
				"Reuther", "Hönig", "Mair", "Jörg", "Möhring", "Haak", "Mehl", "Grohmann", "Gerth", "Wölfel", "Dammann",
				"Schupp", "Schad", "Markus", "Hilgers", "Blaschke", "Rosenbaum", "Borgmann", "Leicht", "Eichinger",
				"Lüders", "Frese", "Wrede", "Linde", "Höpfner", "Kube", "Nestler", "Joos", "Penner", "Lieb", "Schaal",
				"Drechsel", "Kessel", "Seiffert", "Bellmann", "Stolze", "Ruff", "Weinberger", "Schuck", "Rummel",
				"Seel", "Sonnenberg", "Hüttner", "Schmidbauer", "Neff", "Klement", "Wittke", "Schmiedel", "Liebscher",
				"Bogner", "Erhard", "Burkert", "Boll", "Stehle", "Krings", "Baumbach", "Schöning", "Kohlmann",
				"Schwarzkopf", "Schaffer", "Mehnert", "Kreft", "Schreiter", "Junghans", "Hilpert", "Althaus",
				"Messerschmidt", "Marten", "Haake", "Rech", "Böse", "Panzer", "Schlicht", "Höfler", "Köppen",
				"Eberlein", "Schillinger", "Falke", "Klos", "Belz", "Michalski", "Borowski", "Pott", "Loose", "Mauer",
				"Kurtz", "Taubert", "Heindl", "Mock", "Krull", "Hammerschmidt", "Hahne", "Bickel", "Rautenberg", "Koll",
				"Hähnel", "Göpfert", "Küchler", "Stöckl", "Goller", "Korb", "Söllner", "Hornig", "Dressel", "Wuttke",
				"Schiefer", "Heinzelmann", "Rank", "Staab", "Wiebe", "Holtz", "Richert", "Frerichs", "Flemming", "Hepp",
				"Both", "Degenhardt", "Lippmann", "Utz", "Kittel", "Eckl", "Yildirim", "Schmied", "Köpke", "Geiß",
				"Dohmen", "Zielinski", "Kautz", "Burmester", "Bluhm", "Krone", "Thiem", "Kindler", "Potthoff",
				"Mueller", "Bell", "Aydin", "Straube", "Klar", "Riegel", "Reichmann", "Kluth", "Dittmer", "Welzel",
				"Kümmel", "Holland", "Bücker", "Imhof", "Lewandowski", "Beckert", "Schreyer", "Ehret", "Ruppel",
				"Kaczmarek", "Wiegmann", "Feige", "Träger", "Buss", "Brugger", "Assmann", "Olschewski", "Meinert",
				"Gundlach", "Zacharias", "Straßer", "Risse", "Pauls", "Heins", "Stelter", "Speer", "Bier", "Teske",
				"Knebel", "Hufnagel", "Mewes", "Windisch", "Warnke", "Frost", "Volkmer", "Schwind", "Reisinger",
				"Debus", "Bopp", "Schill", "Bley", "Mischke", "Böhnke", "Meinecke", "Flach", "Günzel", "Klier",
				"Bausch", "Teufel", "Brühl", "Schultheiß", "Oehler", "Hallmann", "Stich", "Meisel", "Krumm", "Hänel",
				"Rathmann", "Leistner", "Domke", "Sigl", "Wenk", "Weigert", "Laue", "Schlecht", "Reiners", "Krenz",
				"Baer", "Kirstein", "Bußmann", "Ganz", "Rössler", "Dietzel", "Karsten", "Schäfers", "Böckmann",
				"Pöhlmann", "Kampmann", "Yildiz", "Borrmann", "Kähler", "Kettner", "Kollmann", "Platz", "Feist",
				"Weyer", "Oldenburg", "Niehaus", "Kölbl", "Weinzierl", "Gutsche", "Dressler", "Zuber", "Buhr", "Garbe",
				"Swoboda", "Moos", "Kröner", "Holzner", "Abt", "Reichl", "Nielsen", "Schanz", "Pollmann", "Hipp",
				"Schimmel", "Thielen", "Schultheis", "Nebel", "Steininger", "Jürgensen", "Zinke", "Behringer", "Wald",
				"Matzke", "Stiegler", "Schmuck", "Dobler", "Bürkle", "Rasche", "Eller", "Bade", "Stenger", "Knuth",
				"Seufert", "Jehle", "Saß", "Roller", "Taube", "Linnemann", "Neuner", "Neuber", "Mathes", "Fetzer",
				"Röhr", "Knauf", "Holstein", "Haufe", "Fleckenstein", "Risch", "Philippi", "Merker", "Kerber",
				"Weitzel", "Scheuer", "Klenk", "Keppler", "Özdemir", "Ostermeier", "Tesch", "Steck", "Knoop", "Kircher",
				"Ehmann", "Knoblauch", "Öztürk", "Rühle", "Koopmann", "Leuschner", "Laufer", "Schorn", "Friebe", "Mark",
				"Knabe", "Leber", "Lauber", "Stürmer", "Putz", "Nägele", "Meiners", "Weidlich", "Püschel", "Beermann",
				"Sippel", "Schalk", "Reger", "Heumann", "Stang", "Zühlke", "Milde", "Kindermann", "Weingärtner",
				"Harnisch", "Groll", "Waibel", "Grosch", "Priebe", "Wellmann", "Marek", "Freier", "Schießl", "Kelm",
				"Hetzel", "Langen", "Kusch", "Hild", "Grosser", "Fehr", "Geßner", "Bruch", "Bohne", "Duda", "Tewes",
				"Stockmann", "Roscher", "Sievert", "Deckert", "Bethke", "Kirch", "Kraemer", "Gürtler", "Sperber",
				"Valentin", "Rüter", "Lippold", "Beil", "Schorr", "Kind", "Ziemann", "Wiechmann", "Schuchardt", "Kuntz",
				"Burkart", "Holtmann", "Welker", "Leipold", "Kistner", "Kaspar", "Griese", "Schimpf", "Schier",
				"Lenzen", "Polster", "Wichert", "Klumpp", "Hintze", "Staiger", "Gabel", "Balke", "Severin", "Kipp",
				"Dogan", "Hintz", "Goldbach", "Stecher", "Meindl", "Pflug", "Lamm", "Witzel", "Thaler", "Rumpf",
				"Demuth", "Plank", "Pick", "Obermaier", "Kloos", "Deutschmann", "Bongartz", "Biehl", "Löser",
				"Teschner", "Roloff", "Kahlert", "Lietz", "Distler", "Breitenbach", "Thoms", "Kloss", "Hutter",
				"Gaiser", "Pichler", "Krahl", "Brink", "Spielmann", "Seibt", "Kunath", "Stüber", "Zellner", "Scholten",
				"Reinartz", "Ihle", "Wernicke", "Kirschbaum", "Moldenhauer", "Schuch", "Hansmann", "Burkard", "Back",
				"Bitter", "Licht", "Kapp", "Stracke", "Heinisch", "Grossmann", "Weichert", "Sutter", "Matt",
				"Hielscher", "Clasen", "Adolph", "Steinbrecher", "Kothe", "Hey", "Grill", "Brümmer", "Voit", "Vater",
				"Fuhr", "Eisenmann", "Storz", "Herwig", "Nordmann", "George", "Kappes", "Beutel", "Stender", "Klöckner",
				"Angerer", "Kast", "Flügel", "Gold", "Steinberger", "Heyne", "Handke", "Brose", "Stolte", "Lutter",
				"Roll", "Kalb", "Spangenberg", "Ilg", "Behrends", "Daub", "Rüther", "Goldschmidt", "Rinke", "Menge",
				"Greif", "Wehrle", "Riese", "Pries", "Lambrecht", "Gerling", "Michl", "Emrich", "Jentzsch", "Sorg",
				"Hundt", "Kamm", "Holzinger", "Wohlfahrt", "Knittel", "Freyer", "Wedel", "Mönch", "Offermann",
				"Schaarschmidt", "Kaufhold", "Wurster", "Tetzlaff", "Schug", "Köllner", "Pfisterer", "Salewski",
				"Schnelle", "Ertel", "Thom", "Kloß", "Epp", "Friedmann", "Fenske", "Rehberg", "Knaus", "Gräber",
				"Wittenberg", "Plate", "Mittag", "Junk", "Strube", "Mäder", "Köhne", "Bäumer", "Maschke", "Kuschel",
				"Kerner", "Herter", "Stemmer", "Nürnberger", "Nicolai", "Claßen", "Stefan", "Puls", "Gerner",
				"Radermacher", "Germann", "Flick", "Zwick", "Clauß", "Wiest", "Storm", "Harrer", "Lüdemann", "Kögel",
				"Kullmann", "Sieg", "Schäffler", "Schenkel", "Jahns", "Dierks", "Leder", "Franzke", "Retzlaff",
				"Marschner", "Hölzel", "Weidmann", "Ohm", "Helfrich", "Böker", "Reiss", "Nau", "Mehlhorn", "Lambert",
				"Herzberg", "Bechtold", "Dahms", "Hannig", "Biller", "Wollny", "Meiser", "Raddatz", "Blessing",
				"Scheidt", "Lennartz", "Kunzmann", "Falkenberg", "Naujoks", "Maaßen", "Kaltenbach", "Göhring", "Arend",
				"Köppe", "Jochum", "Rist", "Mauch", "Bernard", "Thum", "Oltmanns", "Limmer", "Gunkel", "Wall",
				"Niebuhr", "Leis", "Wedekind", "Völkl", "Alber", "Weitz", "Reh", "Kerscher", "Ruß", "Hammes",
				"Alexander", "Andreas", "Wassermann", "Roßmann", "Freudenberg", "Stach", "Klostermann", "Johannes",
				"Heinzmann", "Bertsch", "de Vries", "Greulich", "Piel", "Brosch", "Galle", "Cornelius", "Seubert",
				"Plath", "Dröge", "Birkner", "Thome", "Nießen", "Bäumler", "Scheffel", "Sanders", "Klingler", "Eckel",
				"Schlenker", "Spahn", "Mühl", "Heyn", "Grams", "Brummer", "Bacher", "Tischler", "Acker", "Hohl",
				"Diederich", "Knoche", "Schöbel", "Schätzle", "Lind", "Krupp", "Klasen", "Klaas", "Streicher",
				"Scheele", "Heer", "Felber", "Schellenberg", "Heiden", "Haberland", "Rosin", "Holm", "Rump", "Büchler",
				"Plum", "Matheis", "Härtl", "Frahm", "Meder", "Epple", "Görlich", "Arslan", "Rehbein", "Freytag",
				"Kötter", "Ferber", "Amend", "Mau", "Neitzel", "Lautenschläger", "Dahlke", "Adrian", "Scheck",
				"Reinsch", "Plötz", "Wilk", "Wenger", "Kutscher", "Kappel", "Mund", "Mandel", "Wehr", "Menges",
				"Zoller", "Schewe", "Zeiler", "Wehrmann", "Kutz", "Häuser", "Faulhaber", "Schunk", "Bast",
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
