package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NATIONALITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfJoining;
import seedu.address.model.person.Dob;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nationality;
import seedu.address.model.person.Note;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_NRIC,
                        PREFIX_GENDER, PREFIX_DOB, PREFIX_DATE, PREFIX_NATIONALITY, PREFIX_ADDRESS, PREFIX_TAG);

        StringBuilder missingFieldsHeader = new StringBuilder("Missing required field: ");
        StringBuilder missingFieldsExamples = new StringBuilder("Eg: ");
        boolean hasMissingFields = false;

        if (!argMultimap.getValue(PREFIX_NAME).isPresent()) {
            missingFieldsHeader.append("name (n/) ");
            missingFieldsExamples.append("n/John Doe ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            missingFieldsHeader.append("phone (p/) ");
            missingFieldsExamples.append("p/91234567 ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            missingFieldsHeader.append("email (e/) ");
            missingFieldsExamples.append("e/johndoe@example.com ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_NRIC).isPresent()) {
            missingFieldsHeader.append("NRIC (ic/) ");
            missingFieldsExamples.append("ic/S1234567D ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_GENDER).isPresent()) {
            missingFieldsHeader.append("gender (g/) ");
            missingFieldsExamples.append("g/Male ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_DOB).isPresent()) {
            missingFieldsHeader.append("date of birth (d/) ");
            missingFieldsExamples.append("d/20-03-1990 ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_DATE).isPresent()) {
            missingFieldsHeader.append("date of joining (j/) ");
            missingFieldsExamples.append("j/15-04-2023 ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_NATIONALITY).isPresent()) {
            missingFieldsHeader.append("nationality (nat/) ");
            missingFieldsExamples.append("nat/Singaporean ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            missingFieldsHeader.append("address (a/) ");
            missingFieldsExamples.append("a/123 Main Street #05-01/119278 ");
            hasMissingFields = true;
        }
        if (!argMultimap.getValue(PREFIX_TAG).isPresent()) {
            missingFieldsHeader.append("tag (t/) ");
            missingFieldsExamples.append("t/Marketing/Full-Time/Product Manager ");
            hasMissingFields = true;
        }

        // If any fields are missing, throw ParseException with specific message
        if (hasMissingFields) {
            throw new ParseException(
                missingFieldsHeader.toString().trim() + "\n"
                + missingFieldsExamples.toString().trim()
            );
        }

        // Check for non-empty preamble
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_NRIC,
                PREFIX_GENDER, PREFIX_DOB, PREFIX_DATE, PREFIX_NATIONALITY, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Nric nric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        Gender gender = ParserUtil.parseGender(argMultimap.getValue(PREFIX_GENDER).get());
        Dob dob = ParserUtil.parseDob(argMultimap.getValue(PREFIX_DOB).get());
        DateOfJoining dateOfJoining = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());

        if (!dateOfJoining.toLocalDate().isAfter(dob.toLocalDate())) {
            throw new ParseException("Date of Joining must be after DOB.");
        }

        Nationality nationality = ParserUtil.parseNationality(argMultimap.getValue(PREFIX_NATIONALITY).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Note note = new Note("");
        Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());

        Person person = new Person(name, phone, email, nric, gender, dob, dateOfJoining,
                nationality, address, note, tag);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
