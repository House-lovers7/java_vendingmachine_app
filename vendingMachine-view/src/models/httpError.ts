export default interface HttpErrorResponse {
    response: Data;
}

interface Data {
    data: ErrorObject[];
}

interface ErrorObject {
    errMsg: string;
    errCd: string;
}
