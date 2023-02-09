package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.ClientExcursionServiceFactory;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientTooLateToCancelReservaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class AppServiceClient {
    public static void main(String[] args) {
        // TODO
        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientExcursionService clientExcursionService = ClientExcursionServiceFactory.getService();
        if("-addExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4, 5}, new int[] {3});
            // [add]    -addExc <city> <description> <date> <price> <maxPlaces>
            try {
                Long excursionId = clientExcursionService.addExcursion(new ClientExcursionDto(null,
                        args[1], args[2], LocalDateTime.parse(args[3]),
                        Float.valueOf(args[4]), Short.parseShort(args[5]),(short) 0));

                System.out.println("Excursion " + excursionId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-reserve".equalsIgnoreCase(args[0])) {
            // -reserve <userMail> <excursionId> <creditCardNumber> <places>
            validateArgs(args,5,new int[]{2,4},new int[] {});

            Long reservaId;
            try{
                reservaId =clientExcursionService.addReserva(args[1],Long.parseLong(args[2]),
                        args[3],Short.parseShort(args[4]));
                System.out.println("Reservation for excursion (id="+args[2]+") made. Reservation id= "+reservaId);
            } catch (InstanceNotFoundException | InputValidationException e) {
                e.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        } else if("-updateExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {5, 6}, new int[] {4});

            // [update] -updateExc <excId> <city> <description> <date> <price> <maxPlaces>

            try {
                clientExcursionService.updateExcursion(new ClientExcursionDto(
                        Long.valueOf(args[1]),
                        args[2], args[3], LocalDateTime.parse(args[4]), Float.valueOf(args[5]), Short.valueOf(args[6]),(short)0));

                System.out.println("Excursion " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                    InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }


        } else if("-cancel".equalsIgnoreCase(args[0])) {
            // -cancel <reservationId> <userEmail>
            validateArgs(args,3,new int[]{1},new int[] {});

            try{
                clientExcursionService.cancelReserva(Long.parseLong(args[1]),args[2]);
                System.out.println("Cancellation successful");
            } catch (InstanceNotFoundException | InputValidationException | ClientTooLateToCancelReservaException e ) {
                e.printStackTrace(System.err);
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        } else if("-findExcursions".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {}, new int[] {2, 3});

            // [find] -findExcursions <city> <fromDate> <toDate>

            try {
                List<ClientExcursionDto> excursiones = clientExcursionService.findExcursiones(LocalDate.parse(args[2]),
                        LocalDate.parse(args[3]), args[1]);
                System.out.println("Found " + excursiones.size() +
                        " excursion (s) between date '" + args[2] + "' and date '" + args[3] + "'");
                for (int i = 0; i < excursiones.size(); i++) {
                    ClientExcursionDto excursionDto = excursiones.get(i);
                    System.out.println(excursionDto.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findReservations".equalsIgnoreCase(args[0])) {
            // -findReservations <userEmail>
            validateArgs(args,2,new int[]{},new int[] {});

            try{
                List<ClientReservaDto> reservas = clientExcursionService.findReserva(args[1]);
                System.out.println("Found " + reservas.size() +
                        " reservation(s) with userMail '" + args[1] + "'");
                for (ClientReservaDto reserva : reservas){
                    System.out.println(reserva.toString());
                }
            }catch (Exception ex){ex.printStackTrace(System.err);}
        }
    }
    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments, int[] dateArguments) {
        if(expectedArgs != args.length) {
            System.out.println("Error: Argumentos Erróneos");
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double num = Double.parseDouble(args[position]);
                if(num <= 0){
                    throw new InputValidationException("Argumentos Numéricos erróneos");
                }
            } catch(NumberFormatException | InputValidationException n) {
                System.out.println("Error: Argumentos Numéricos erróneos");
                printUsageAndExit();
            }
        }
        for(int i = 0 ; i< dateArguments.length ; i++) {
            int position = dateArguments[i];
            try {
                LocalDateTime date = null;
                if(args[position].length() == 10){
                    date = LocalDate.parse(args[position]).atStartOfDay();
                } else {
                    date = LocalDateTime.parse(args[position]);
                }
                if(date.isBefore(LocalDateTime.now())){
                    throw new InputValidationException("La fecha introducida es anterior al día de hoy");
                }
            } catch(DateTimeException | InputValidationException n) {
                System.out.println("Error: Argumentos de Fecha Mal Introducidos");
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addExcursion]      AppServiceClient -addExc <city> <description> <date> <price> <maxPlaces> \n" +
                "    [reserve]           AppServiceClient -reserve <userMail> <excursionId> <creditCardNumber> <places> \n" +
                "    [updateExcursion]   AppServiceClient -updateExc <excId> <city> <description> <date> <price> <maxPlaces> \n" +
                "    [cancelReservation] AppServiceClient -cancel <reservationId> <userEmail>\n" +
                "    [findExcursions]    AppServiceClient -findExcursions <city> <fromDate> <toDate>\n" +
                "    [findReservations]  AppServiceClient -findReservations <userEmail>\n");
    }
}