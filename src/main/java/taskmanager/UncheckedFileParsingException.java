package taskmanager;

/**
 * Unchecked-обёртка над FileParsingException.
 * Нужна, чтобы бросить ошибку разбора строки изнутри лямбды в Stream API —
 * функциональные интерфейсы вроде Function/Predicate не умеют объявлять
 * throws для checked-исключений, поэтому checked заворачиваем в unchecked,
 * а снаружи стрима — разворачиваем обратно.
 */
public class UncheckedFileParsingException extends RuntimeException {

    public UncheckedFileParsingException(FileParsingException cause) {
        super(cause);
    }

    @Override
    public FileParsingException getCause() {
        return (FileParsingException) super.getCause();
    }
}
