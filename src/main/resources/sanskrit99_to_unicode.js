// Modified from:
// http://hindi-fonts.com/tools/sanskrit99-to-Unicode-Converter

function convert_to_unicode(input_text) {

	var array_one = new Array(

	"ß", "ë", "å", "ã", "â", "Þ", "Û", "×", "Ò", "Ð", "Í", "²", "±", "°", // additions missing from original
			
	"`", "ç", "ð", "¬", "ñ", "Õ", "¶", "í", "¹", "¾", "Æ", "Ó", "Ù",

	"ù", "û", "ü", "ý", "ÿ", "’", "þ", "Ÿ",

	"³", "µ", "¢", "º", "À", "¿", "†", "q+", "f+", "F+",

	"Èy", "È", "Ç", "Ô", "Ø", "Ü", "à", "ä", "æ", "è", "ì", "ï", "ö", "ô",

	"Š", "ò", "ó", "Ï", "˜", "Ý", "}", "ú", "]", "Ö", "´", "Ú", "”", "Î", "Ì", "Ñ",

	"»", "¼", "Â", "Ã", "=", "•", "Å", "Ä", "“", "‘", "*", "'(", "D(", "H(", "q(", "Q(", "f(", "F(",

	"½", "õ", "®", "î", "(",

	"k…", "kª", "'…", "'ª", "Ê", "Ë", ")…", ")ª", "é", "ê", "÷", "ø", "ˆK", "ˆO", "ˆG", "ˆJ", "ˆá", "ˆk", "ˆo", "ˆg", "ˆj", "–f", "–F",
			"ˆ)", "k", "K", "Š", "o", "O", "g", "G", "¸", "\"", "'", "c", "C", "D", "j", "J", "H", "Á", "|", "q", "Q", "f", "F", "[", "{a",
			"{", "t", "T", "w", "W", "d", "x", "X", "n", "N", "p", "P", ")", "á", "b", "B", "É", "_", "m", "M", "y", "Y", "r", "+", "l",
			"L", "¦", "v", "V", "z", "Z", ";", ":", "s", "S", "h", "AaE", "Aae", "Aa£", "Aa", "A", "#", "$", "%", "^", "@e", "@", "\\",
			"§", "¤", "¥", "„", "ae", "aE", "a", "I", "u", "…", "‚", "U", "ª", "ƒ", "«", "¯", "&", "¨", "©", "e", "E", "<", "—", "~", "£",
			">", "!", "œ", ",", ".", "/", "?",

			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

	var array_two = new Array(
			
	"प्न", "व्न", "भ्न", "ब्न", "ब्ज", "प्त्", "न्न्", "ध्न", "द्म", "द्न", "द्ग्र", "क्न", "न्क्र", "ॄ",

	"ॐ", "म्न", "श्ल", "क्ल", "श्व", "द्र्य", "ग्न", "श्च", "घ्न", "ज्ज", "त्न", "द्ब", "न्त्र",

	"हृ", "ह्न", "ह्म", "ह्य", "ह्र", "ह्ल", "ह्व", "ह्ण",

	"क्र", "ख्र", "ग्र", "घ्र", "छ्र", "ज्र", "दृ", "ट्र", "ड्र", "ढ्र",

	"त्र्य", "त्र्", "त्र", "द्र", "ध्र", "न्र", "प्र", "ब्र", "भ्र", "म्र", "व्र", "श्र", "स्र", "स्त्र",

	"क्क", "ष्ट", "ष्ठ", "द्ध", "द्घ", "प्त", "ज्ञ", "क्ष्", "क्ष", "द्व", "क्त", "न्न", "ट्ट", "द्द", "द्ग", "द्भ",

	"ङ्क", "ङ्ग", "ञ्च", "ञ्ज", "ऽ", ".", "त्त्", "त्त", "ङ्क्त", "ल्ल", "द्य", "ङ्य", "छ्य", "झ्य", "ट्य", "ठ्य", "ड्य", "ढ्य",

	"च्च", "स्न", "क्त्", "श्न", "्य",

	"कु", "कू", "ङु", "ङू", "दु", "दू", "फु", "फू", "रु", "रू", "हु", "हू", "क़्", "ख़्", "ग़्", "ज़्", "फ़्", "क़", "ख़", "ग़", "ज़",
			"ड़", "ढ़", "फ़", "क", "क्", "क्", "ख", "ख्", "ग", "ग्", "घ", "घ", "ङ", "च", "च्", "छ", "ज", "ज्", "झ", "ञ्", "ञ", "ट", "ठ",
			"ड", "ढ", "ण", "ण", "ण्", "त", "त्", "थ", "थ्", "द", "ध", "ध्", "न", "न्", "प", "प्", "फ", "फ्", "ब", "ब्", "भ", "भ्", "म",
			"म्", "य", "य्", "र", "र", "ल", "ल्", "ळ", "व", "व्", "श", "श्", "ष", "ष्", "स", "स्", "ह", "औ", "ओ", "ऑ", "आ", "अ", "इ", "ई",
			"उ", "ऊ", "ऐ", "ए", "ऋ", "ॠ", "ऌ", "ॡ", "कॢ", "ो", "ौ", "ा", "ी", "ु", "ु", "ु", "ू", "ू", "ू", "ृ", "ृ", "ृ", "ॄ", "ॄ", "े",
			"ै", "ं", "ं", "ँ", "ॅ", ":", "्", "्", "।", "॥", "॒", "॑",

			"०", "१", "२", "३", "४", "५", "६", "७", "८", "९")

	// ********************************************************
	// Special glyph "¡" (reph+anusvAr)
	// ********************************************************

	var array_one_length = array_one.length;

	// initialize variable for Replace_Symbols() function to modify
	// e.g. a valid string would be
	// var modified_substring = 'ivvahidnimd< Évtu h;Rdm!,';
	var modified_substring = input_text;

	Replace_Symbols();

	var processed_text = modified_substring;

	processed_text = processed_text.replace(/Sanskrit 99/g, "mangal");

	return processed_text;

	// --------------------------------------------------
	function Replace_Symbols()

	{

		// substitute array_two elements in place of corresponding array_one
		// elements

		if (modified_substring != "") // if stringto be converted is non-blank
										// then no need of any processing.
		{
			for (input_symbol_idx = 0; input_symbol_idx < array_one_length; input_symbol_idx++)

			{

				idx = 0; // index of the symbol being searched for
							// replacement

				while (idx != -1) // whie-00
				{

					modified_substring = modified_substring.replace(array_one[input_symbol_idx], array_two[input_symbol_idx])
					idx = modified_substring.indexOf(array_one[input_symbol_idx])

				} // end of while-00 loop
			} // end of for loop

			// **********************************************************************************
			// Code for Replacing Special glyph : "¡" (reph+anusvAr)
			// **********************************************************************************
			modified_substring = modified_substring.replace(/¡/g, "Rं"); // at
																			// some
																			// places
																			// ¡ is
																			// used
																			// e.g.
																			// in
																			// "कर्कंधु,पूर्णांक".

			// **********************************************************************************
			// code for replacing "i" with "ि" (chhotee ee kii maatraa) and
			// correcting its position too.(moving it one position forward)
			// **********************************************************************************

			var position_of_i = modified_substring.indexOf("i")

			while (position_of_i != -1) // while-02
			{
				var charecter_next_to_i = modified_substring.charAt(position_of_i + 1)
				var charecter_to_be_replaced = "i" + charecter_next_to_i
				modified_substring = modified_substring.replace(charecter_to_be_replaced, charecter_next_to_i + "ि")
				position_of_i = modified_substring.search(/i/, position_of_i + 1) // search
																					// for
																					// i
																					// ahead
																					// of
																					// the
																					// current
																					// position.

			} // end of while-02 loop

			// following loop to eliminate 'chhotee ee kee maatraa' on
			// half-letters as a result of above transformation.

			var position_of_wrong_ee = modified_substring.indexOf("ि्")

			while (position_of_wrong_ee != -1) // while-03

			{
				var consonent_next_to_wrong_ee = modified_substring.charAt(position_of_wrong_ee + 2)
				var charecter_to_be_replaced = "ि्" + consonent_next_to_wrong_ee
				modified_substring = modified_substring.replace(charecter_to_be_replaced, "्" + consonent_next_to_wrong_ee + "ि")
				position_of_wrong_ee = modified_substring.search(/ि्/, position_of_wrong_ee + 2) // search
																									// for
																									// 'wrong
																									// ee'
																									// ahead
																									// of
																									// the
																									// current
																									// position.

			} // end of while-03 loop

			// Eliminating "R" and putting 'half - r' at proper position for
			// this.
			set_of_matras = "ा ॉ ि ी ु ू ृ े ै ो ौ ं : ँ ॅ"

			// alert(" 1. modified_substring = "+modified_substring );

			var position_of_R = modified_substring.indexOf("R")

			// alert(" 2. position_of_R = "+position_of_R )
			// ************************************************************
			while (position_of_R > 0) // while-01
			{
				probable_position_of_half_r = position_of_R - 1;

				// alert(" 3. probable_position_of_half_r =
				// "+probable_position_of_half_r );

				var character_at_probable_position_of_half_r = modified_substring.charAt(probable_position_of_half_r)

				// alert(" 4. character_at_probable_position_of_half_r =
				// "+character_at_probable_position_of_half_r );

				// ************************************************************
				// trying to find non-maatra position left to current O (ie,
				// half -r).
				// ************************************************************
				while (set_of_matras.match(character_at_probable_position_of_half_r) != null) // while-02
				// some vowel maatraa or anusvaar found, move to previous
				// character
				{
					probable_position_of_half_r = probable_position_of_half_r - 1;
					character_at_probable_position_of_half_r = modified_substring.charAt(probable_position_of_half_r);

					// alert(" 5. probable_position_of_half_r =
					// "+probable_position_of_half_r );
					// alert(" 6. character_at_probable_position_of_half_r =
					// "+character_at_probable_position_of_half_r );
				} // end of while-02

				// ************************************************************
				// check if the previous character to the present character is a
				// halant
				// ************************************************************
				var previous_to_position_of_half_r = probable_position_of_half_r - 1;
				// alert(" 7. previous_to_position_of_half_r =
				// "+previous_to_position_of_half_r );

				if (previous_to_position_of_half_r > 0) // if-03
				{
					var character_previous_to_position_of_half_r = modified_substring.charAt(previous_to_position_of_half_r)
					// alert(" 8. character_previous_to_position_of_half_r =
					// "+character_previous_to_position_of_half_r );

					while ("्".match(character_previous_to_position_of_half_r) != null) // while-04
					// halant found, move to previous character
					{
						probable_position_of_half_r = previous_to_position_of_half_r - 1;
						character_at_probable_position_of_half_r = modified_substring.charAt(probable_position_of_half_r);

						// alert(" 9. probable_position_of_half_r =
						// "+probable_position_of_half_r );
						// alert("10. character_at_probable_position_of_half_r =
						// "+character_at_probable_position_of_half_r );

						previous_to_position_of_half_r = probable_position_of_half_r - 1;
						character_previous_to_position_of_half_r = modified_substring.charAt(previous_to_position_of_half_r)

						// alert("11. previous_to_position_of_half_r =
						// "+previous_to_position_of_half_r );
						// alert("12. character_previous_to_position_of_half_r =
						// "+character_previous_to_position_of_half_r );
					} // end of while-04
				} // end of if-03

				// ************************************************************
				character_to_be_replaced = modified_substring.substr(probable_position_of_half_r,
						(position_of_R - probable_position_of_half_r));

				new_replacement_string = "र्" + character_to_be_replaced;
				character_to_be_replaced = character_to_be_replaced + "R";
				modified_substring = modified_substring.replace(character_to_be_replaced, new_replacement_string);

				// alert("13. character_to_be_replaced =
				// "+character_to_be_replaced );
				// alert("14. modified_substring = "+modified_substring );

				position_of_R = modified_substring.indexOf("R");
			} // end of while-01

		} // end of IF statement meant to supress processing of blank string.

	} // end of the function Replace_Symbols
	
} // end of convert_to_unicode function

