package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {

	public static Stage stageAtual(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static <T> void formatarTableColumnDate(TableColumn<T, Date> tableColumn, String formato) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> celula = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(formato);

				@Override
				protected void updateItem(Date item, boolean vazio) {
					super.updateItem(item, vazio);
					if (vazio) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return celula;
		});
	}

	public static <T> void formatarTableColumnDouble(TableColumn<T, Double> tableColumn, int casasDecimais) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> celula = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean vazio) {
					super.updateItem(item, vazio);
					if (vazio) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + casasDecimais + "f", item));
					}
				}
			};
			return celula;
		});
	}

	public static void formatarDatePicker(DatePicker datePicker, String formato) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formato);
			{
				datePicker.setPromptText(formato.toLowerCase());
			}

			@Override
			public String toString(LocalDate data) {
				if (data != null) {
					return dateFormatter.format(data);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}

}
