package responses;

import dataAccess.AuthDAO;

public record LoginResponse(AuthDAO authDAO) {
}
