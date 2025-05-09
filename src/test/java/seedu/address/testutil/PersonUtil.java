package seedu.address.testutil;

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

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_NRIC + person.getNric().value + " ");
        sb.append(PREFIX_GENDER + person.getGender().value + " ");
        sb.append(PREFIX_DOB + person.getDob().value + " ");
        sb.append(PREFIX_DATE + person.getDateOfJoining().value + " ");
        sb.append(PREFIX_NATIONALITY + person.getNationality().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        String[] tagList = person.getTag().getValue();
        String department = tagList[0];
        String employmentType = tagList[1];
        String jobTitle = tagList[2];
        sb.append(PREFIX_TAG + department + "/" + employmentType + "/" + jobTitle);
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getGender().ifPresent(gender -> sb.append(PREFIX_GENDER).append(gender.value).append(" "));
        descriptor.getDob().ifPresent(dob -> sb.append(PREFIX_DOB).append(dob.value).append(" "));
        descriptor.getDateOfJoining().ifPresent(dateOfJoining -> sb.append(PREFIX_DATE)
                .append(dateOfJoining.value).append(" "));
        descriptor.getNationality().ifPresent(nationality -> sb.append(PREFIX_NATIONALITY)
                .append(nationality.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getTag().ifPresent(tag -> sb.append(PREFIX_TAG)
                .append(tag.getValue()[0])
                .append("/")
                .append(tag.getValue()[1])
                .append("/")
                .append(tag.getValue()[2]));
        return sb.toString();
    }
}
