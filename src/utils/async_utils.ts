export class AsyncUtils {

    public static each<T>(list: T[], cb: (item: T, index: number) => any): Promise<any> {

        const promises = list.map((item, index) => {
            return cb(item, index);
        });

        return Promise.all(promises);
    }

}